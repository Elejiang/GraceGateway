package com.grace.gateway.core.resilience;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.grace.gateway.common.enums.CircuitBreakerEnum;
import com.grace.gateway.config.manager.DynamicConfigManager;
import com.grace.gateway.config.pojo.RouteDefinition;
import io.github.resilience4j.bulkhead.*;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ResilienceFactory {

    private static final Map<String, Retry> retryMap = new ConcurrentHashMap<>();
    private static final Map<String, CircuitBreaker> circuitBreakerMap = new ConcurrentHashMap<>();
    private static final Map<String, Bulkhead> bulkheadMap = new ConcurrentHashMap<>();
    private static final Map<String, ThreadPoolBulkhead> threadPoolBulkheadMap = new ConcurrentHashMap<>();

    private static final Set<String> retrySet = new ConcurrentHashSet<>();
    private static final Set<String> circuitBreakerSet = new ConcurrentHashSet<>();
    private static final Set<String> bulkheadSet = new ConcurrentHashSet<>();
    private static final Set<String> threadPoolBulkheadSet = new ConcurrentHashSet<>();

    public static Retry buildRetry(RouteDefinition.ResilienceConfig resilienceConfig, String serviceName) {
        if (!resilienceConfig.isRetryEnabled()) {
            return null;
        }
        return retryMap.computeIfAbsent(serviceName, name -> {
            if (!retrySet.contains(serviceName)) {
                DynamicConfigManager.getInstance().addRouteListener(serviceName, newRoute -> retryMap.remove(newRoute.getServiceName()));
                retrySet.add(serviceName);
            }
            RetryConfig config = RetryConfig.custom()
                    .maxAttempts(resilienceConfig.getMaxAttempts())
                    .waitDuration(Duration.ofMillis(resilienceConfig.getWaitDuration()))
                    .build();
            return RetryRegistry.of(config).retry(serviceName);
        });
    }

    public static CircuitBreaker buildCircuitBreaker(RouteDefinition.ResilienceConfig resilienceConfig, String serviceName) {
        if (!resilienceConfig.isCircuitBreakerEnabled()) {
            return null;
        }
        return circuitBreakerMap.computeIfAbsent(serviceName, name -> {
            if (!circuitBreakerSet.contains(serviceName)) {
                DynamicConfigManager.getInstance().addRouteListener(serviceName, newRoute -> circuitBreakerMap.remove(newRoute.getServiceName()));
                circuitBreakerSet.add(serviceName);
            }
            CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                    .failureRateThreshold(resilienceConfig.getFailureRateThreshold())
                    .slowCallRateThreshold(resilienceConfig.getSlowCallRateThreshold())
                    .waitDurationInOpenState(Duration.ofMillis(resilienceConfig.getWaitDurationInOpenState()))
                    .slowCallDurationThreshold(Duration.ofSeconds(resilienceConfig.getSlowCallDurationThreshold()))
                    .permittedNumberOfCallsInHalfOpenState(resilienceConfig.getPermittedNumberOfCallsInHalfOpenState())
                    .minimumNumberOfCalls(resilienceConfig.getMinimumNumberOfCalls())
                    .slidingWindowType(slidingWindowTypeConvert(resilienceConfig.getType()))
                    .slidingWindowSize(resilienceConfig.getSlidingWindowSize())
                    .build();
            return CircuitBreakerRegistry.of(circuitBreakerConfig).circuitBreaker(serviceName);
        });
    }

    public static Bulkhead buildBulkHead(RouteDefinition.ResilienceConfig resilienceConfig, String serviceName) {
        if (!resilienceConfig.isBulkheadEnabled()) {
            return null;
        }
        return bulkheadMap.computeIfAbsent(serviceName, name -> {
            if (!bulkheadSet.contains(serviceName)) {
                DynamicConfigManager.getInstance().addRouteListener(serviceName, newRoute -> bulkheadMap.remove(newRoute.getServiceName()));
                bulkheadSet.add(serviceName);
            }
            BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
                    .maxConcurrentCalls(resilienceConfig.getMaxConcurrentCalls())
                    .maxWaitDuration(Duration.ofMillis(resilienceConfig.getMaxWaitDuration()))
                    .fairCallHandlingStrategyEnabled(resilienceConfig.isFairCallHandlingEnabled()).build();
            return BulkheadRegistry.of(bulkheadConfig).bulkhead(serviceName);
        });
    }

    public static ThreadPoolBulkhead buildThreadPoolBulkhead(RouteDefinition.ResilienceConfig resilienceConfig, String serviceName) {
        if (!resilienceConfig.isThreadPoolBulkheadEnabled()) {
            return null;
        }
        return threadPoolBulkheadMap.computeIfAbsent(serviceName, name -> {
            if (!threadPoolBulkheadSet.contains(serviceName)) {
                DynamicConfigManager.getInstance().addRouteListener(serviceName, newRoute -> threadPoolBulkheadMap.remove(newRoute.getServiceName()));
                threadPoolBulkheadSet.add(serviceName);
            }
            ThreadPoolBulkheadConfig threadPoolBulkheadConfig = ThreadPoolBulkheadConfig.custom()
                    .coreThreadPoolSize(resilienceConfig.getCoreThreadPoolSize())
                    .maxThreadPoolSize(resilienceConfig.getMaxThreadPoolSize())
                    .queueCapacity(resilienceConfig.getQueueCapacity())
                    .build();
            return ThreadPoolBulkheadRegistry.of(threadPoolBulkheadConfig).bulkhead(serviceName);
        });
    }

    private static CircuitBreakerConfig.SlidingWindowType slidingWindowTypeConvert(CircuitBreakerEnum from) {
        if (from == CircuitBreakerEnum.TIME_BASED) {
            return CircuitBreakerConfig.SlidingWindowType.TIME_BASED;
        } else {
            return CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;
        }
    }

}
