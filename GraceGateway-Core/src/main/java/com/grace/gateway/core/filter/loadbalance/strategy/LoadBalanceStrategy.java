package com.grace.gateway.core.filter.loadbalance.strategy;

import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.core.context.GatewayContext;

import java.util.List;

public interface LoadBalanceStrategy {

    ServiceInstance selectInstance(GatewayContext context, List<ServiceInstance> instances);

    String mark();

}
