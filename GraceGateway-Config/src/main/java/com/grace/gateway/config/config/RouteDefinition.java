package com.grace.gateway.config.config;

import lombok.Data;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

@Data
public class RouteDefinition {

    // 路由id
    private String id = UUID.randomUUID().toString();

    // 服务名
    private String serviceName;

    // 路由的URI
    private URI uri;

    // 路由顺序，当请求匹配到多个路由时，选择顺序小的
    private int order = 0;

    // 超时时间，单位ms
    private int timeout = 3000;

    // 重试次数
    private int retryTimes = 3;

    // 路由需要走的过滤器
    private Set<FilterConfig> filterConfigs;


    @Data
    public static class FilterConfig {

        /**
         * 过滤器名字，唯一的
         */
        private String name;

        /**
         * 过滤器规则描述，json
         */
        private String config;

    }

}
