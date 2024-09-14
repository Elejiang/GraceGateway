package com.grace.gateway.core.filter.loadbalance;

import com.grace.gateway.core.filter.loadbalance.strategy.LoadBalanceStrategy;
import com.grace.gateway.core.filter.loadbalance.strategy.RoundRobinLoadBalanceStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;


@Slf4j
public class LoadBalanceStrategyManager {

    private static final Map<String, LoadBalanceStrategy> strategyMap = new HashMap<>();

    static {
        ServiceLoader<LoadBalanceStrategy> serviceLoader = ServiceLoader.load(LoadBalanceStrategy.class);
        for (LoadBalanceStrategy strategy : serviceLoader) {
            strategyMap.put(strategy.mark(), strategy);
            log.info("load loadbalance strategy success: {}", strategy);
        }
    }

    public static LoadBalanceStrategy getStrategy(String name) {
        LoadBalanceStrategy strategy = strategyMap.get(name);
        if (strategy == null)
            strategy = new RoundRobinLoadBalanceStrategy();
        return strategy;
    }

}
