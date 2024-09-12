package com.grace.gateway.core.filter.gray;

import com.grace.gateway.core.filter.gray.strategy.GrayStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;


@Slf4j
public class GrayStrategyManager {

    private static final Map<String, GrayStrategy> strategyMap = new HashMap<>();

    static {
        ServiceLoader<GrayStrategy> serviceLoader = ServiceLoader.load(GrayStrategy.class);
        for (GrayStrategy strategy : serviceLoader) {
            strategyMap.put(strategy.mark(), strategy);
            log.info("load gray strategy success: {}", strategy);
        }
    }

    public static GrayStrategy getStrategy(String name) {
        return strategyMap.get(name);
    }

}
