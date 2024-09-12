package com.grace.gateway.core.filter.loadbalance.strategy;

import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.core.context.GatewayContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.grace.gateway.common.constant.LoadBalanceConstant.RANDOM_LOAD_BALANCE_STRATEGY;

public class RandomLoadBalanceStrategy implements LoadBalanceStrategy {

    @Override
    public ServiceInstance selectInstance(GatewayContext context, List<ServiceInstance> instances) {
        return instances.get(ThreadLocalRandom.current().nextInt(instances.size()));
    }

    @Override
    public String mark() {
        return RANDOM_LOAD_BALANCE_STRATEGY;
    }

}
