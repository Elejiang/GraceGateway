package com.grace.gateway.register.service;

import com.grace.gateway.config.config.Config;

/**
 * 注册中心接口
 */
public interface RegisterCenterProcessor {

    /**
     * 注册中心初始化
     */
    void init(Config config);

    /**
     * 订阅注册中心实例变化
     */
    void subscribeServiceChange(RegisterCenterListener listener);

}
