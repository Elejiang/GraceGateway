package com.grace.gateway.core.flow;

import com.grace.gateway.core.context.GatewayContext;

public interface RateLimiter {

    void tryConsume(GatewayContext context);

}
