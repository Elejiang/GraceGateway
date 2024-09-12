package com.grace.gateway.core.filter.loadbalance;

import com.grace.gateway.config.manager.DynamicConfigManager;
import com.grace.gateway.config.pojo.ServiceInstance;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.Filter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static com.grace.gateway.common.constant.FilterConstant.LOAD_BALANCE_FILTER_NAME;
import static com.grace.gateway.common.constant.FilterConstant.LOAD_BALANCE_FILTER_ORDER;

@Slf4j
public class LoadBalanceFilter implements Filter {

    @Override
    public void doFilter(GatewayContext context) {
        // 获取服务所有实例
        Collection<ServiceInstance> instances = DynamicConfigManager.getInstance()
                .getInstancesByServiceName(context.getRequest().getServiceDefinition().getServiceName())
                .values();

    }

    @Override
    public String mark() {
        return LOAD_BALANCE_FILTER_NAME;
    }

    @Override
    public int getOrder() {
        return LOAD_BALANCE_FILTER_ORDER;
    }

}
