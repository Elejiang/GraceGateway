package com.grace.gateway.core.filter.flow;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.grace.gateway.config.manager.DynamicConfigManager;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.util.FilterUtil;
import com.grace.gateway.core.algorithm.LeakyBucketRateLimiter;
import com.grace.gateway.core.algorithm.SlidingWindowRateLimiter;
import com.grace.gateway.core.algorithm.TokenBucketRateLimiter;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.Filter;
import io.netty.channel.EventLoop;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.grace.gateway.common.constant.FilterConstant.FLOW_FILTER_NAME;
import static com.grace.gateway.common.constant.FilterConstant.FLOW_FILTER_ORDER;

public class FlowFilter implements Filter {

    private final ConcurrentHashMap<String /* 服务名 */, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    private final Set<String> addListener = new ConcurrentHashSet<>();

    @Override
    public void doPreFilter(GatewayContext context) {

        RouteDefinition.FlowFilterConfig flowFilterConfig = Optional
                .ofNullable(FilterUtil.findFilterConfigByClass(context.getRoute().getFilterConfigs(), FLOW_FILTER_NAME, RouteDefinition.FlowFilterConfig.class))
                .orElse(new RouteDefinition.FlowFilterConfig());

        if (!flowFilterConfig.isEnabled()) { // 如果没有开启流控
            context.doFilter();
        } else {
            String serviceName = context.getRequest().getServiceDefinition().getServiceName();
            RateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(serviceName, name -> {
                if (!addListener.contains(name)) {
                    DynamicConfigManager.getInstance().addRouteListener(name, newRoute -> {
                        rateLimiterMap.remove(newRoute.getServiceName());
                    });
                    addListener.add(name);
                }
                return initRateLimiter(flowFilterConfig, context.getNettyCtx().channel().eventLoop());
            });
            rateLimiter.tryConsume(context);
        }
    }

    @Override
    public void doPostFilter(GatewayContext context) {
        context.doFilter();
    }

    @Override
    public String mark() {
        return FLOW_FILTER_NAME;
    }

    @Override
    public int getOrder() {
        return FLOW_FILTER_ORDER;
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private RateLimiter initRateLimiter(RouteDefinition.FlowFilterConfig flowFilterConfig, EventLoop eventLoop) {
        switch (flowFilterConfig.getType()) {
            case TOKEN_BUCKET -> {
                return new TokenBucketRateLimiter(flowFilterConfig.getCapacity(), flowFilterConfig.getRate());
            }
            case SLIDING_WINDOW -> {
                return new SlidingWindowRateLimiter(flowFilterConfig.getCapacity(), flowFilterConfig.getRate());
            }
            case LEAKY_BUCKET -> {
                return new LeakyBucketRateLimiter(flowFilterConfig.getCapacity(), flowFilterConfig.getRate(), eventLoop);
            }
            default -> {
                return new TokenBucketRateLimiter(flowFilterConfig.getCapacity(), flowFilterConfig.getRate());
            }
        }
    }

}
