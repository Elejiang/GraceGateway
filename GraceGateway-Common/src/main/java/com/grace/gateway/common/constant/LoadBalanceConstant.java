package com.grace.gateway.common.constant;

public interface LoadBalanceConstant {

    String ROUND_ROBIN_LOAD_BALANCE_STRATEGY = "round_robin_load_balance_strategy"; // 轮询策略

    String WEIGHT_LOAD_BALANCE_STRATEGY = "weight_load_balance_strategy"; // 权重策略

    String RANDOM_LOAD_BALANCE_STRATEGY = "random_load_balance_strategy"; // 随机策略

    String GRAY_LOAD_BALANCE_STRATEGY = "gray_load_balance_strategy"; // 灰度流量的策略

}
