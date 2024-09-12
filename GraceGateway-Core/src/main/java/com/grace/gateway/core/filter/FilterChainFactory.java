package com.grace.gateway.core.filter;

import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class FilterChainFactory {

    private static final Map<String, Filter> filterMap = new ConcurrentHashMap<>();

    static {
        ServiceLoader<Filter> serviceLoader = ServiceLoader.load(Filter.class);
        for (Filter filter : serviceLoader) {
            filterMap.put(filter.mark(), filter);
            log.info("load filter success: {}", filter);
        }
    }

    public static FilterChain buildFilterChain(GatewayContext ctx) {
        FilterChain chain = new FilterChain();

        addPreFilter(chain);
        addFilter(chain, ctx.getRoute().getFilterConfigs());
        addPostFilter(chain);

        return chain;
    }

    private static void addPreFilter(FilterChain chain) {

    }

    private static void addFilter(FilterChain chain, Set<RouteDefinition.FilterConfig> filterConfigs) {
        for (RouteDefinition.FilterConfig filterConfig : filterConfigs) {
            Filter filter = filterMap.get(filterConfig.getName());
            if (null != filter) {
                chain.add(filter);
            } else {
                log.info("not found filter: {}", filterConfig.getName());
            }
        }
    }

    private static void addPostFilter(FilterChain chain) {

    }


}
