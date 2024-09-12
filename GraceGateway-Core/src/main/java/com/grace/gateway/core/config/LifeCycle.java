package com.grace.gateway.core.config;

public interface LifeCycle {

    /**
     * 启动
     */
    void start();

    /**
     * 关闭
     */
    void shutdown();

    /**
     * 是否启动
     */
    boolean isStarted();

}
