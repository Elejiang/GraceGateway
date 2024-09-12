package com.grace.gateway.core.filter;

import com.grace.gateway.core.context.GatewayContext;

public interface Filter {

    void doFilter(GatewayContext context);

    String mark(); // 标识唯一的过滤器

    int getOrder();

}
