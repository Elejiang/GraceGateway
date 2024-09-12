package com.grace.gateway.common.constant;

public interface LoadBalanceConstant {

    int VIRTUAL_NODE_NUM = 100; // 一致性哈希算法虚拟节点个数

    String ROUND_ROBIN_LOAD_BALANCE_STRATEGY = "round_robin_load_balance_strategy"; // 轮询策略

    String WEIGHT_LOAD_BALANCE_STRATEGY = "weight_load_balance_strategy"; // 权重策略

    String RANDOM_LOAD_BALANCE_STRATEGY = "random_load_balance_strategy"; // 随机策略

    String GRAY_LOAD_BALANCE_STRATEGY = "gray_load_balance_strategy"; // 灰度流量的策略

    String CLIENT_IP_LOAD_BALANCE_STRATEGY = "client_ip_load_balance_strategy"; // 客户端ip策略，同个客户端会被路由到相同实例上

    String CLIENT_IP_CONSISTENT_HASH_LOAD_BALANCE_STRATEGY = "client_ip_consistent_hash_load_balance_strategy"; // 根据请求ip的一致性哈希策略

}
