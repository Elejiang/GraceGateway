package com.grace.gateway.core.resilience;


import com.grace.gateway.common.enums.ResponseCode;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.route.RouteUtil;
import com.grace.gateway.core.helper.ContextHelper;
import com.grace.gateway.core.helper.ResponseHelper;
import com.grace.gateway.core.resilience.fallback.FallbackHandler;
import com.grace.gateway.core.resilience.fallback.FallbackHandlerManager;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.asynchttpclient.Response;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;


public class Resilience {

    private static final Resilience INSTANCE = new Resilience();

    ScheduledExecutorService retryScheduler = Executors.newScheduledThreadPool(10);

    private Resilience() {}

    public static Resilience getInstance() {
        return INSTANCE;
    }

    public void executeRequest(GatewayContext gatewayContext) {
        RouteDefinition.ResilienceConfig resilienceConfig = gatewayContext.getRoute().getResilience();
        String serviceName = gatewayContext.getRequest().getServiceDefinition().getServiceName();

        Retry retry = ResilienceFactory.buildRetry(resilienceConfig, serviceName);
        CircuitBreaker circuitBreaker = ResilienceFactory.buildCircuitBreaker(resilienceConfig, serviceName);

        Supplier<CompletionStage<Response>> supplier = RouteUtil.buildRouteSupplier(gatewayContext);
        if (retry != null) {
            supplier = Retry.decorateCompletionStage(retry, retryScheduler, supplier);
        }
        if (circuitBreaker != null) {
            supplier = CircuitBreaker.decorateCompletionStage(circuitBreaker, supplier);
        }

        supplier.get().exceptionally(throwable -> { // 发送熔断
            if (resilienceConfig.isFallbackEnabled()) { // 执行降级
                FallbackHandler handler = FallbackHandlerManager.getHandler(resilienceConfig.getFallbackHandlerName());
                handler.handle(throwable, gatewayContext);
            } else {
                gatewayContext.setThrowable(throwable);
                gatewayContext.setResponse(ResponseHelper.buildGatewayResponse(ResponseCode.SERVICE_UNAVAILABLE));
                ContextHelper.writeBackResponse(gatewayContext);
            }
            return null;
        });
    }

}
