package com.grace.gateway.config.service.impl.zookeeper;

import com.grace.gateway.config.config.ConfigCenter;
import com.grace.gateway.config.service.AbstractConfigCenterProcessor;
import com.grace.gateway.config.service.RoutesChangeListener;

// TODO Zookeeper配置中心实现
public class ZookeeperConfigCenter extends AbstractConfigCenterProcessor {

    public ZookeeperConfigCenter(ConfigCenter configCenter) {
        super(configCenter);
    }

    protected void initialize() {

    }

    protected void subscribeConfigChange(RoutesChangeListener listener) {

    }
}
