package com.grace.gateway.core.test;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class TestResilience {

    @Test
    public void testResilience() throws ExecutionException, InterruptedException {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        RetryRegistry retryRegistry = RetryRegistry.ofDefaults();

        CircuitBreakerConfig circuitBreakerConfig = circuitBreakerRegistry.getDefaultConfig();

        CircuitBreaker circuitBreaker = CircuitBreakerRegistry.of(circuitBreakerConfig).circuitBreaker("cb test");
        Retry retry = retryRegistry.retry("retry test");

        Supplier<CompletionStage<Void>> supplier = () -> CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务");
            throw new RuntimeException("Simulated exception");
        });

        Supplier<CompletionStage<Void>> retrySupplier = Retry.decorateCompletionStage(retry, Executors.newScheduledThreadPool(10), supplier);

        CompletionStage<Void> stringCompletionStage = retrySupplier.get();


        System.out.println(stringCompletionStage.toCompletableFuture()
                .exceptionally(throwable -> {
                    System.out.println("重试三次后异常：" + throwable);
                    return null;
                }).get());

        Thread.sleep(10000);
    }

}
