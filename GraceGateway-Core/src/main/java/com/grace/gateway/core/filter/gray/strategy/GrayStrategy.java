package com.grace.gateway.core.filter.gray.strategy;

import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.core.context.GatewayContext;

import java.util.Collection;

public interface GrayStrategy {

    boolean shouldRoute2Gray(GatewayContext context, Collection<ServiceInstance> instances);

    String mark();

}
