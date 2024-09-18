package com.grace.gateway.core.flow;

import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.algorithm.LeakyBucketRateLimiter;
import com.grace.gateway.core.algorithm.SlidingWindowRateLimiter;
import com.grace.gateway.core.algorithm.TokenBucketRateLimiter;
import com.grace.gateway.core.context.GatewayContext;
import io.netty.channel.EventLoop;

import java.util.concurrent.ConcurrentHashMap;

public class FlowProcessor {

    // TODO 缓存一致
    private final ConcurrentHashMap<String /* 服务名 */, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    private FlowProcessor() {}

    private static final FlowProcessor INSTANCE = new FlowProcessor();

    public static FlowProcessor getInstance() {
        return INSTANCE;
    }

    public void doFlow(GatewayContext context, RouteDefinition.FlowConfig flowConfig) {
        String serviceName = context.getRequest().getServiceDefinition().getServiceName();
        RateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(serviceName,
                key -> initRateLimiter(flowConfig, context.getNettyCtx().channel().eventLoop()));
        rateLimiter.tryConsume(context);
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private RateLimiter initRateLimiter(RouteDefinition.FlowConfig flowConfig, EventLoop eventLoop) {
        switch (flowConfig.getType()) {
            case TOKEN_BUCKET -> {
                return new TokenBucketRateLimiter(flowConfig.getCapacity(), flowConfig.getRate());
            }
            case SLIDING_WINDOW -> {
                return new SlidingWindowRateLimiter(flowConfig.getCapacity(), flowConfig.getRate());
            }
            case LEAKY_BUCKET -> {
                return new LeakyBucketRateLimiter(flowConfig.getCapacity(), flowConfig.getRate(), eventLoop);
            }
            default -> {
                return new TokenBucketRateLimiter(flowConfig.getCapacity(), flowConfig.getRate());
            }
        }
    }

}
