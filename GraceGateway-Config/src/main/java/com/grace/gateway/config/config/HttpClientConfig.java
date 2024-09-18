package com.grace.gateway.config.config;

import lombok.Data;

@Data
public class HttpClientConfig {

    private int eventLoopGroupWorkerNum = Runtime.getRuntime().availableProcessors() * 2; // worker数量

    private int httpConnectTimeout = 30 * 1000; // 连接超时时间

    private int httpRequestTimeout = 30 * 1000; // 请求超时时间

    private int httpMaxRedirects = 2; // 客户端最大重定向次数

    private int httpMaxConnections = 10000; // 客户端请求最大连接数

    private int httpConnectionsPerHost = 8000; // 客户端每个地址支持的最大连接数

    private int httpPooledConnectionIdleTimeout = 60 * 1000; // 客户端空闲连接超时时间, 默认60秒

}
