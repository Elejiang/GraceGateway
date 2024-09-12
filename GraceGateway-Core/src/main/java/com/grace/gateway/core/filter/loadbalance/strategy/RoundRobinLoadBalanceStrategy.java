package com.grace.gateway.core.filter.loadbalance.strategy;

import cn.hutool.json.JSONUtil;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.config.util.FilterUtil;
import com.grace.gateway.core.context.GatewayContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.grace.gateway.common.constant.FilterConstant.LOAD_BALANCE_FILTER_NAME;
import static com.grace.gateway.common.constant.LoadBalanceConstant.ROUND_ROBIN_LOAD_BALANCE_STRATEGY;

public class RoundRobinLoadBalanceStrategy implements LoadBalanceStrategy {

    private final AtomicInteger strictPosition = new AtomicInteger(0);

    private int position = 0;

    private final int THRESHOLD = Integer.MAX_VALUE >> 2; // 预防移除的安全阈值

    @Override
    public ServiceInstance selectInstance(GatewayContext context, List<ServiceInstance> instances) {
        boolean isStrictRoundRobin = true;
        RouteDefinition.FilterConfig filterConfig = FilterUtil.findFilterConfigByName(context.getRoute().getFilterConfigs(), LOAD_BALANCE_FILTER_NAME);
        if (filterConfig != null) {
            RouteDefinition.LoadBalanceFilterConfig loadBalanceFilterConfig = JSONUtil.toBean(filterConfig.getConfig(), RouteDefinition.LoadBalanceFilterConfig.class);
            isStrictRoundRobin = loadBalanceFilterConfig.isStrictRoundRobin();
        }
        ServiceInstance serviceInstance;
        if (isStrictRoundRobin) {
            int index = Math.abs(strictPosition.getAndIncrement());
            serviceInstance = instances.get(index % instances.size());
            if (index >= THRESHOLD) {
                strictPosition.set((index + 1) % instances.size());
            }
        } else {
            int index = Math.abs(position++);
            serviceInstance = instances.get(index % instances.size());
            if (position >= THRESHOLD) {
                position = (position + 1) % instances.size();
            }
        }
        return serviceInstance;
    }

    @Override
    public String mark() {
        return ROUND_ROBIN_LOAD_BALANCE_STRATEGY;
    }

}
