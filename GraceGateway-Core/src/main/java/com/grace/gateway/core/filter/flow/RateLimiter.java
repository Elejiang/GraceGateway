package com.grace.gateway.core.filter.flow;

import com.grace.gateway.core.context.GatewayContext;

public interface RateLimiter {

    void tryConsume(GatewayContext context);

}
