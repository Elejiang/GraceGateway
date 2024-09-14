package com.grace.gateway.core.resilience.fallback;

import com.grace.gateway.core.context.GatewayContext;

public interface FallbackHandler {

    void handle(Throwable throwable, GatewayContext context);

    String mark();

}
