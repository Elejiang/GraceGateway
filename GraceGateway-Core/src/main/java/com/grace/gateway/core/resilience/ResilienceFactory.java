package com.grace.gateway.core.resilience;

import com.grace.gateway.common.enums.CircuitBreakerEnum;
import com.grace.gateway.config.pojo.RouteDefinition;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;

// TODO 缓存
public class ResilienceFactory {

    public static Retry buildRetry(RouteDefinition.ResilienceConfig resilienceConfig, String serviceName) {
        if (!resilienceConfig.isRetryEnabled()) {
            return null;
        }
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(resilienceConfig.getMaxAttempts())
                .waitDuration(Duration.ofMillis(resilienceConfig.getWaitDuration()))
                .build();
        return RetryRegistry.of(config).retry(serviceName);
    }

    public static CircuitBreaker buildCircuitBreaker(RouteDefinition.ResilienceConfig resilienceConfig, String serviceName) {
        if (!resilienceConfig.isCircuitBreakerEnabled()) {
            return null;
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
    }

    private static CircuitBreakerConfig.SlidingWindowType slidingWindowTypeConvert(CircuitBreakerEnum from) {
        if (from == CircuitBreakerEnum.TIME_BASED) {
            return CircuitBreakerConfig.SlidingWindowType.TIME_BASED;
        } else {
            return CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;
        }
    }

}
