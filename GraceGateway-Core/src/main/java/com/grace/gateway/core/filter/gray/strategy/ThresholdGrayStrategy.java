package com.grace.gateway.core.filter.gray.strategy;

import cn.hutool.json.JSONUtil;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.config.util.FilterUtil;
import com.grace.gateway.core.context.GatewayContext;

import java.util.Collection;

import static com.grace.gateway.common.constant.FilterConstant.GRAY_FILTER_NAME;
import static com.grace.gateway.common.constant.GrayConstant.THRESHOLD_GRAY_STRATEGY;

/**
 * 根据流量决定是否灰度策略
 */
public class ThresholdGrayStrategy implements GrayStrategy {

    @Override
    public boolean shouldRoute2Gray(GatewayContext context, Collection<ServiceInstance> instances) {
        if (instances.stream().anyMatch(instance -> instance.isEnabled() && !instance.isGray())) {
            RouteDefinition.FilterConfig filterConfig = FilterUtil.findFilterConfigByName(context.getRoute().getFilterConfigs(), GRAY_FILTER_NAME);
            RouteDefinition.GrayFilterConfig grayFilterConfig = JSONUtil.toBean(filterConfig.getConfig(), RouteDefinition.GrayFilterConfig.class);
            double grayThreshold = instances.stream().mapToDouble(ServiceInstance::getThreshold).sum();
            grayThreshold = Math.min(grayThreshold, grayFilterConfig.getMaxGrayThreshold());
            return Math.abs(Math.random() - 1) <= grayThreshold;
        }
        return true;
    }

    @Override
    public String mark() {
        return THRESHOLD_GRAY_STRATEGY;
    }

}
