package com.grace.gateway.core.filter.gray.strategy;

import cn.hutool.json.JSONUtil;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.config.util.FilterUtil;
import com.grace.gateway.core.context.GatewayContext;

import java.util.List;

import static com.grace.gateway.common.constant.FilterConstant.GRAY_FILTER_NAME;
import static com.grace.gateway.common.constant.GrayConstant.CLIENT_IP_GRAY_STRATEGY;

public class ClientIpGrayStrategy implements GrayStrategy {

    @Override
    public boolean shouldRoute2Gray(GatewayContext context, List<ServiceInstance> instances) {
        if (instances.stream().anyMatch(instance -> instance.isEnabled() && !instance.isGray())) {
            String host = context.getRequest().getHost();
            RouteDefinition.FilterConfig filterConfig = FilterUtil.findFilterConfigByName(context.getRoute().getFilterConfigs(), GRAY_FILTER_NAME);
            RouteDefinition.GrayFilterConfig grayFilterConfig = JSONUtil.toBean(filterConfig.getConfig(), RouteDefinition.GrayFilterConfig.class);
            double grayThreshold = instances.stream().mapToDouble(ServiceInstance::getThreshold).sum();
            grayThreshold = Math.min(grayThreshold, grayFilterConfig.getMaxGrayThreshold());
            return Math.abs(host.hashCode()) % 100 <= grayThreshold * 100;
        }
        return true;
    }

    @Override
    public String mark() {
        return CLIENT_IP_GRAY_STRATEGY;
    }

}
