package com.grace.gateway.config.pojo;

import com.grace.gateway.common.enums.CircuitBreakerEnum;
import com.grace.gateway.common.enums.FlowEnum;
import com.grace.gateway.common.enums.ResilienceEnum;
import lombok.Data;

import java.util.*;

import static com.grace.gateway.common.constant.FallbackConstant.DEFAULT_FALLBACK_HANDLER_NAME;
import static com.grace.gateway.common.constant.GrayConstant.MAX_GRAY_THRESHOLD;
import static com.grace.gateway.common.constant.GrayConstant.THRESHOLD_GRAY_STRATEGY;
import static com.grace.gateway.common.constant.LoadBalanceConstant.ROUND_ROBIN_LOAD_BALANCE_STRATEGY;
import static com.grace.gateway.common.constant.LoadBalanceConstant.VIRTUAL_NODE_NUM;
import static com.grace.gateway.common.enums.FlowEnum.TOKEN_BUCKET;
import static com.grace.gateway.common.enums.ResilienceEnum.*;

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

    // 系统弹性配置，熔断、降级、重试等
    private ResilienceConfig resilience = new ResilienceConfig();

    // 路由需要走的过滤器
    private Set<FilterConfig> filterConfigs = new HashSet<>();

    @Data
    public static class ResilienceConfig {

        private boolean enabled = true; // 是否开启弹性配置

        private boolean retryEnabled = true; // 是否开启重试
        private boolean circuitBreakerEnabled = true; // 是否开启熔断
        private boolean fallbackEnabled = true; // 是否开启降级
        private boolean bulkheadEnabled = false; // 是否开启信号量隔离
        private boolean threadPoolBulkheadEnabled = false; // 是否开启线程池隔离

        private List<ResilienceEnum> order = Arrays.asList(THREADPOOLBULKHEAD, BULKHEAD, RETRY, CIRCUITBREAKER, FALLBACK);

        // Retry
        private int maxAttempts = 3; // 重试次数
        private int waitDuration = 500; // 重试间隔

        // CircuitBreaker
        private int failureRateThreshold = 50; // 以百分比配置失败率阈值。当失败率大于等于阈值时，进行熔断，并进行服务降级
        private int slowCallRateThreshold = 100; // 慢调用比例超过这个则进行熔断，并进行服务降级
        private int slowCallDurationThreshold = 60000; // 单位ms，超过这个视为慢调用，这个应该需要比httpclient的请求超时时间httpRequestTimeout大，否则不会生效
        private int permittedNumberOfCallsInHalfOpenState = 10; // 断路器在半开状态下允许通过的调用次数
        private int maxWaitDurationInHalfOpenState = 0; // 断路器在半开状态下的最长等待时间，超过该配置值的话，断路器会从半开状态恢复为开启状态。配置是0时表示断路器会一直处于半开状态，直到所有允许通过的访问结束
        private CircuitBreakerEnum type = CircuitBreakerEnum.COUNT_BASED; // 滑动窗口类型，如果是COUNT_BASED，则是计数，如果是TIME_BASED，则是时间，单位是秒
        private int slidingWindowSize = 100; // 滑动窗口大小
        private int minimumNumberOfCalls = 100; // 统计失败率或慢调用率的最小调用数
        private int waitDurationInOpenState = 60000; // 断路器从开启过渡到半开应等待的时间，单位ms
        private boolean automaticTransitionFromOpenToHalfOpenEnabled = false; // 是否开启额外线程监听断路器从开启到半开的状态变化，如果不开启，则需时间到了并且有请求才会到半开状态

        // Fallback
        private String fallbackHandlerName = DEFAULT_FALLBACK_HANDLER_NAME; // 默认降级策略名

        // Bulkhead
        private int maxConcurrentCalls = 1000; // 信号数量
        private int maxWaitDuration = 0; // 最大等待时间
        private boolean fairCallHandlingEnabled = false; // 是否公平竞争信号量

        // ThreadPoolBulkhead
        private int coreThreadPoolSize = 5; // 核心线程数
        private int maxThreadPoolSize = 10; // 最大线程数
        private int queueCapacity = 100; // 队列容量

    }

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

    @Data
    public static class LoadBalanceFilterConfig {

        /**
         * 负载均衡策略名，默认轮询
         */
        private String strategyName = ROUND_ROBIN_LOAD_BALANCE_STRATEGY;

        /**
         * 是否开启严格轮询
         */
        private boolean isStrictRoundRobin = true;

        /**
         * 一致性哈希算法虚拟节点个数
         */
        private int virtualNodeNum = VIRTUAL_NODE_NUM;

    }

    @Data
    public static class FlowFilterConfig {

        /**
         * 是否开启流控
         */
        private boolean enabled = false;

        /**
         * 流控类型
         */
        private FlowEnum type = TOKEN_BUCKET;

        /**
         * 容量
         */
        private int capacity = 1000;

        /**
         * 速率
         * 如果是滑动窗口则是窗口大小，单位 ms
         * 如果是令牌桶，则是令牌桶生成速率，单位 个/s
         * 如果是漏桶，则是漏桶速率，单位 ms/个
         */
        private int rate = 500;

    }
}






















