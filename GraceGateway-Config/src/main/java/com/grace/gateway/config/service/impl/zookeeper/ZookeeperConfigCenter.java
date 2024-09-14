package com.grace.gateway.config.service.impl.zookeeper;

import com.grace.gateway.config.config.ConfigCenter;
import com.grace.gateway.config.service.ConfigCenterProcessor;
import com.grace.gateway.config.service.RoutesChangeListener;

import java.util.concurrent.atomic.AtomicBoolean;

// TODO Zookeeper配置中心实现
public class ZookeeperConfigCenter implements ConfigCenterProcessor {

    /**
     * 配置
     */
    private ConfigCenter configCenter;

    /**
     * 是否完成初始化
     */
    private final AtomicBoolean init = new AtomicBoolean(false);

    @Override
    public void init(ConfigCenter configCenter) {
        if (!configCenter.isEnabled() || !init.compareAndSet(false, true)) {
            return;
        }
        this.configCenter = configCenter;
    }

    @Override
    public void subscribeRoutesChange(RoutesChangeListener listener) {
        if (!configCenter.isEnabled() || !init.get()) {
            return;
        }
    }
}
