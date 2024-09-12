package com.grace.gateway.core.filter.loadbalance.strategy;

import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.core.context.GatewayContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.grace.gateway.common.constant.LoadBalanceConstant.WEIGHT_LOAD_BALANCE_STRATEGY;

public class WeightLoadBalanceStrategy implements LoadBalanceStrategy {

    @Override
    public ServiceInstance selectInstance(GatewayContext context, List<ServiceInstance> instances) {
        int totalWeight = instances.stream().mapToInt(ServiceInstance::getWeight).sum();
        if (totalWeight <= 0) return null;
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);
        for (ServiceInstance instance : instances) {
            randomWeight -= instance.getWeight();
            if (randomWeight < 0) return instance;
        }
        return null;
    }

    @Override
    public String mark() {
        return WEIGHT_LOAD_BALANCE_STRATEGY;
    }

}
