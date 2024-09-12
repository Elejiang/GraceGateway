package com.grace.gateway.core.filter.gray.strategy;

import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.core.context.GatewayContext;

import java.util.List;

public interface GrayStrategy {

    boolean shouldRoute2Gray(GatewayContext context, List<ServiceInstance> instances);

    String mark();

}
