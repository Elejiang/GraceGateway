package com.grace.gateway.core.algorithm;

import com.grace.gateway.common.enums.ResponseCode;
import com.grace.gateway.common.exception.LimitedException;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.flow.RateLimiter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketRateLimiter implements RateLimiter {

    private final int capacity; // 桶的容量
    private final int refillRate; // 令牌生成速率
    private final AtomicInteger tokens; // 当前令牌数量

    public TokenBucketRateLimiter(int capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRate = refillRatePerSecond;
        this.tokens = new AtomicInteger(0);
        startRefilling();
    }

    @Override
    public void tryConsume(GatewayContext context) {
        if (tokens.getAndDecrement() > 0) {
            context.doFilter();
        } else {
            tokens.incrementAndGet();
            throw new LimitedException(ResponseCode.TOO_MANY_REQUESTS);
        }
    }

    private void startRefilling() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::refillTokens, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void refillTokens() {
        tokens.set(Math.min(capacity, tokens.get() + refillRate));
    }

}
