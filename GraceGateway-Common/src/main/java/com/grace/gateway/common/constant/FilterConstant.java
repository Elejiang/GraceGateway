package com.grace.gateway.common.constant;

public interface FilterConstant {

    String CORS_FILTER_NAME = "cors_filter"; // 跨域过滤器名字

    int CORS_FILTER_ORDER = Integer.MIN_VALUE; // 跨域过滤器顺序

    String FLOW_FILTER_NAME = "flow_filter"; // 流控过滤器名字

    int FLOW_FILTER_ORDER = Integer.MIN_VALUE + 1; // 流控过滤器顺序

    String GRAY_FILTER_NAME = "gray_filter"; // 灰度过滤器名字

    int GRAY_FILTER_ORDER = Integer.MIN_VALUE + 2; // 灰度过滤器顺序

    String LOAD_BALANCE_FILTER_NAME = "load_balance_filter"; // 负载均衡过滤器名字

    int LOAD_BALANCE_FILTER_ORDER = Integer.MIN_VALUE + 3; // 负载均衡过滤器顺序

    String ROUTE_FILTER_NAME = "route_filter"; // 路由过滤器名字

    int ROUTE_FILTER_ORDER = Integer.MAX_VALUE; // 路由过滤器顺序

}
