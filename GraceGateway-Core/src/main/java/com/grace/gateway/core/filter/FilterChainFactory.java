package com.grace.gateway.core.filter;

import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import static com.grace.gateway.common.constant.FilterConstant.*;


@Slf4j
public class FilterChainFactory {

    private static final Map<String, Filter> filterMap = new HashMap<>();

    static {
        ServiceLoader<Filter> serviceLoader = ServiceLoader.load(Filter.class);
        for (Filter filter : serviceLoader) {
            filterMap.put(filter.mark(), filter);
            log.info("load filter success: {}", filter);
        }
    }

    public static void buildFilterChain(GatewayContext ctx) {
        FilterChain chain = new FilterChain();

        addPreFilter(chain);
        addFilter(chain, ctx.getRoute().getFilterConfigs());
        addPostFilter(chain);

        ctx.setFilterChain(chain);
    }

    private static void addPreFilter(FilterChain chain) {
        addFilterIfPresent(chain, GRAY_FILTER_NAME);
        addFilterIfPresent(chain, LOAD_BALANCE_FILTER_NAME);
    }

    private static void addFilter(FilterChain chain, Set<RouteDefinition.FilterConfig> filterConfigs) {
        for (RouteDefinition.FilterConfig filterConfig : filterConfigs) {
            if (!addFilterIfPresent(chain, filterConfig.getName())) {
                log.info("not found filter: {}", filterConfig.getName());
            }
        }
    }

    private static void addPostFilter(FilterChain chain) {
        addFilterIfPresent(chain, ROUTE_FILTER_NAME);
    }

    private static boolean addFilterIfPresent(FilterChain chain, String filterName) {
        Filter filter = filterMap.get(filterName);
        if (null != filter) {
            chain.add(filter);
            return true;
        }
        return false;
    }


}
