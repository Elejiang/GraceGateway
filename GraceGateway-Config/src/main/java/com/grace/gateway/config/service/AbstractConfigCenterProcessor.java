package com.grace.gateway.config.service;

import com.grace.gateway.config.config.ConfigCenter;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractConfigCenterProcessor implements ConfigCenterProcessor {

    /**
     * 配置中心信息
     */
    protected final ConfigCenter configCenter;

    /**
     * 是否完成初始化
     */
    private final AtomicBoolean init = new AtomicBoolean(false);

    public AbstractConfigCenterProcessor(ConfigCenter configCenter) {
        this.configCenter = configCenter;
        init();
    }

    @Override
    public void init() {
        if (!configCenter.isEnabled() || !init.compareAndSet(false, true)) {
            return;
        }
        initialize();
    }

    @Override
    public void subscribeRoutesChange(RoutesChangeListener listener) {
        if (!configCenter.isEnabled()) {
            return;
        }
        subscribeConfigChange(listener);
    }

    protected abstract void initialize();

    protected abstract void subscribeConfigChange(RoutesChangeListener listener);

}
