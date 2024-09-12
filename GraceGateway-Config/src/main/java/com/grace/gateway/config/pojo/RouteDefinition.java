package com.grace.gateway.config.pojo;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

import static com.grace.gateway.common.constant.GrayConstant.MAX_GRAY_THRESHOLD;
import static com.grace.gateway.common.constant.GrayConstant.THRESHOLD_GRAY_STRATEGY;

@Data
public class RouteDefinition {

    // 路由id
    private String id = UUID.randomUUID().toString();

    // 服务名
    private String serviceName;

    // 路由的URI，/xxx/yyy/** 的格式
    private String uri;

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
         * 是否启用过滤器
         */
        private boolean enable = true;

        /**
         * 过滤器规则描述，json
         */
        private String config;

    }

    @Data
    public static class GrayFilterConfig {

        /**
         * 灰度策略名，默认根据流量
         */
        private String strategyName = THRESHOLD_GRAY_STRATEGY;

        /**
         * 灰度流量最大比例
         */
        private double maxGrayThreshold = MAX_GRAY_THRESHOLD;

    }

}
