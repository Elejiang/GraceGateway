package com.grace.gateway.core.resilience;


import com.grace.gateway.common.enums.ResilienceEnum;
import com.grace.gateway.common.enums.ResponseCode;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.route.RouteUtil;
import com.grace.gateway.core.helper.ContextHelper;
import com.grace.gateway.core.helper.ResponseHelper;
import com.grace.gateway.core.resilience.fallback.FallbackHandler;
import com.grace.gateway.core.resilience.fallback.FallbackHandlerManager;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.asynchttpclient.Response;

import java.util.concurrent.*;
import java.util.function.Supplier;


public class Resilience {

    private static final Resilience INSTANCE = new Resilience();

    ScheduledExecutorService retryScheduler = Executors.newScheduledThreadPool(10);

    private Resilience() {
    }

    public static Resilience getInstance() {
        return INSTANCE;
    }

    public void executeRequest(GatewayContext gatewayContext) {
        RouteDefinition.ResilienceConfig resilienceConfig = gatewayContext.getRoute().getResilience();
        String serviceName = gatewayContext.getRequest().getServiceDefinition().getServiceName();

        Supplier<CompletionStage<Response>> supplier = RouteUtil.buildRouteSupplier(gatewayContext);

        for (ResilienceEnum resilienceEnum : resilienceConfig.getOrder()) {
            switch (resilienceEnum) {
                case RETRY -> {
                    Retry retry = ResilienceFactory.buildRetry(resilienceConfig, serviceName);
                    if (retry != null) {
                        supplier = Retry.decorateCompletionStage(retry, retryScheduler, supplier);
                    }
                }
                case FALLBACK -> {
                    if (resilienceConfig.isFallbackEnabled()) {
                        Supplier<CompletionStage<Response>> finalSupplier = supplier;
                        supplier = () ->
                                finalSupplier.get().exceptionally(throwable -> {
                                    FallbackHandler handler = FallbackHandlerManager.getHandler(resilienceConfig.getFallbackHandlerName());
                                    handler.handle(throwable, gatewayContext);
                                    return null;
                                });
                    }
                }
                case CIRCUITBREAKER -> {
                    CircuitBreaker circuitBreaker = ResilienceFactory.buildCircuitBreaker(resilienceConfig, serviceName);
                    if (circuitBreaker != null) {
                        supplier = CircuitBreaker.decorateCompletionStage(circuitBreaker, supplier);
                    }
                }
                case BULKHEAD -> {
                    Bulkhead bulkhead = ResilienceFactory.buildBulkHead(resilienceConfig, serviceName);
                    if (bulkhead != null) {
                        supplier = Bulkhead.decorateCompletionStage(bulkhead, supplier);
                    }
                }
                case THREADPOOLBULKHEAD -> {
                    ThreadPoolBulkhead threadPoolBulkhead = ResilienceFactory.buildThreadPoolBulkhead(resilienceConfig, serviceName);
                    if (threadPoolBulkhead != null) {
                        Supplier<CompletionStage<Response>> finalSupplier = supplier;
                        supplier = () -> {
                            CompletionStage<CompletableFuture<Response>> future =
                                    threadPoolBulkhead.executeSupplier(() -> finalSupplier.get().toCompletableFuture());
                            try {
                                return future.toCompletableFuture().get();
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        };
                    }
                }
            }
        }

        supplier.get().exceptionally(throwable -> {
            if (!resilienceConfig.isFallbackEnabled()) {
                gatewayContext.setThrowable(throwable);
                gatewayContext.setResponse(ResponseHelper.buildGatewayResponse(ResponseCode.SERVICE_UNAVAILABLE));
                ContextHelper.writeBackResponse(gatewayContext);
            }
            return null;
        });
    }

}
