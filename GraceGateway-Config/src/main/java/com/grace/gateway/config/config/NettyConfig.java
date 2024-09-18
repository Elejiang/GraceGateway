package com.grace.gateway.config.config;

import lombok.Data;

/**
 * netty配置
 */
@Data
public class NettyConfig {

    private int eventLoopGroupBossNum = 1;

    private int eventLoopGroupWorkerNum = Runtime.getRuntime().availableProcessors() * 2;

    private int maxContentLength = 64 * 1024 * 1024; // 64MB

}
