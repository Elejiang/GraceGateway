package com.grace.gateway.config.helper;

import com.grace.gateway.config.manager.DynamicConfigManager;
import com.grace.gateway.config.pojo.RouteDefinition;

public class RouteResolver {

    private static final DynamicConfigManager manager = DynamicConfigManager.getInstance();

    /**
     * 根据uri解析出对应的路由
     */
    public static RouteDefinition matchingRouteByUri(String uri) {
        return null;
    }

}
