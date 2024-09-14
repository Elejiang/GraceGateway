package com.grace.gateway.config.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.grace.gateway.config.pojo.RouteDefinition;

import java.util.Collection;

import static com.grace.gateway.common.constant.FilterConstant.GRAY_FILTER_NAME;
import static com.grace.gateway.common.constant.FilterConstant.LOAD_BALANCE_FILTER_NAME;

public class FilterUtil {

    public static RouteDefinition.FilterConfig findFilterConfigByName(Collection<RouteDefinition.FilterConfig> filterConfigs, String name) {
        if (name == null || name.isEmpty() || filterConfigs == null || filterConfigs.isEmpty()) return null;
        for (RouteDefinition.FilterConfig filterConfig : filterConfigs) {
            if (filterConfig == null || filterConfig.getName() == null) continue;
            if (filterConfig.getName().equals(name)) {
                return filterConfig;
            }
        }
        return null;
    }

    public static <T> T findFilterConfigByClass(Collection<RouteDefinition.FilterConfig> filterConfigs, String name, Class<T> clazz) {
        RouteDefinition.FilterConfig filterConfig = findFilterConfigByName(filterConfigs, name);
        if (filterConfig == null) return null;
        return BeanUtil.toBean(filterConfig.getConfig(), clazz);
    }

    public static RouteDefinition.FilterConfig buildDefaultGrayFilterConfig() {
        RouteDefinition.FilterConfig filterConfig = new RouteDefinition.FilterConfig();
        filterConfig.setName(GRAY_FILTER_NAME);
        filterConfig.setEnable(true);
        filterConfig.setConfig(JSONUtil.toJsonStr(new RouteDefinition.GrayFilterConfig()));
        return filterConfig;
    }

    public static RouteDefinition.FilterConfig buildDefaultLoadBalanceFilterConfig() {
        RouteDefinition.FilterConfig filterConfig = new RouteDefinition.FilterConfig();
        filterConfig.setName(LOAD_BALANCE_FILTER_NAME);
        filterConfig.setEnable(true);
        filterConfig.setConfig(JSONUtil.toJsonStr(new RouteDefinition.LoadBalanceFilterConfig()));
        return filterConfig;
    }

}
