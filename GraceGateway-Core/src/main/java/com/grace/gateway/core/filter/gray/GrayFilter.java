package com.grace.gateway.core.filter.gray;

import cn.hutool.json.JSONUtil;
import com.grace.gateway.config.manager.DynamicConfigManager;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.config.util.FilterUtil;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.Filter;
import com.grace.gateway.core.filter.gray.strategy.GrayStrategy;
import com.grace.gateway.core.filter.gray.strategy.ThresholdGrayStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Set;

import static com.grace.gateway.common.constant.FilterConstant.GRAY_FILTER_NAME;
import static com.grace.gateway.common.constant.FilterConstant.GRAY_FILTER_ORDER;

@Slf4j
public class GrayFilter implements Filter {

    @Override
    public void doFilter(GatewayContext context) {
        Set<RouteDefinition.FilterConfig> filterConfigs = context.getRoute().getFilterConfigs();
        RouteDefinition.FilterConfig filterConfig = FilterUtil.findFilterConfigByName(filterConfigs, GRAY_FILTER_NAME);
        if (filterConfig == null) {
            filterConfig = FilterUtil.buildDefaultGrayFilterConfig();
        }
        if (!filterConfig.isEnable()) {
            return;
        }

        // 获取服务所有实例
        Collection<ServiceInstance> instances = DynamicConfigManager.getInstance()
                .getInstancesByServiceName(context.getRequest().getServiceDefinition().getServiceName())
                .values();

        if (instances.stream().anyMatch(instance -> instance.isEnabled() && instance.isGray())) {
            // 存在灰度实例
            RouteDefinition.GrayFilterConfig grayFilterConfig = JSONUtil.toBean(filterConfig.getConfig(), RouteDefinition.GrayFilterConfig.class);
            GrayStrategy strategy = selectGrayStrategy(grayFilterConfig);
            context.getRequest().setGray(strategy.shouldRoute2Gray(context, instances));
        } else {
            // 灰度实例都没，不走灰度
            context.getRequest().setGray(false);
        }
    }

    @Override
    public String mark() {
        return GRAY_FILTER_NAME;
    }

    @Override
    public int getOrder() {
        return GRAY_FILTER_ORDER;
    }

    private GrayStrategy selectGrayStrategy(RouteDefinition.GrayFilterConfig grayFilterConfig) {
        GrayStrategy strategy = GrayStrategyManager.getStrategy(grayFilterConfig.getStrategyName());
        if (strategy == null) strategy = new ThresholdGrayStrategy();
        return strategy;
    }

}