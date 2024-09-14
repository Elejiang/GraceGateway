package com.grace.gateway.core.resilience.fallback;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

@Slf4j
public class FallbackHandlerManager {

    private static final Map<String, FallbackHandler> handlerMap = new HashMap<>();

    static {
        ServiceLoader<FallbackHandler> serviceLoader = ServiceLoader.load(FallbackHandler.class);
        for (FallbackHandler handler : serviceLoader) {
            handlerMap.put(handler.mark(), handler);
            log.info("load fallback handler success: {}", handler);
        }
    }

    public static FallbackHandler getHandler(String name) {
        FallbackHandler handler = handlerMap.get(name);
        if (handler == null)
             handler = new DefaultFallbackHandler();
        return handler;
    }

}
