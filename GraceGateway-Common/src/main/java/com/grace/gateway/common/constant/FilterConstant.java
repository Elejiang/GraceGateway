package com.grace.gateway.common.constant;

public interface FilterConstant {

    String GRAY_FILTER_NAME = "gray_filter"; // 灰度过滤器名字

    int GRAY_FILTER_ORDER = Integer.MIN_VALUE; // 灰度过滤器顺序

    String LOAD_BALANCE_FILTER_NAME = "load_balance_filter"; // 负载均衡过滤器名字

    int LOAD_BALANCE_FILTER_ORDER = Integer.MIN_VALUE + 1; // 负载均衡过滤器顺序

}
