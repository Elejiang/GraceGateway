package com.grace.gateway.config.service;


import com.grace.gateway.config.pojo.RouteDefinition;

import java.util.List;

/**
 * 规则变更监听器
 */
public interface RoutesChangeListener {

    /**
     * 路由变更时调用此方法
     */
    void onRoutesChange(List<RouteDefinition> newRoutes);

}
