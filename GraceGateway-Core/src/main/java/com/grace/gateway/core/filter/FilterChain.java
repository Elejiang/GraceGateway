package com.grace.gateway.core.filter;

import com.grace.gateway.core.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Slf4j
public class FilterChain {

    private final List<Filter> filters = new ArrayList<>();

    public FilterChain add(Filter filter) {
        filters.add(filter);
        return this;
    }

    public FilterChain add(List<Filter> filter) {
        filters.addAll(filter);
        return this;
    }

    public GatewayContext doFilter(GatewayContext ctx) {
        if (filters.isEmpty()) {
            return ctx;
        }
        try {
            filters.sort(Comparator.comparingInt(Filter::getOrder));
            for (Filter filter : filters) {
                filter.doFilter(ctx);
            }
        } catch (Exception e) {
            log.error("执行过滤器发生异常,异常信息：{}", e.getMessage());
            throw e;
        }
        return ctx;
    }
}
