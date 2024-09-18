package com.grace.gateway.bootstrap;

import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.loader.ConfigLoader;
import com.grace.gateway.config.manager.DynamicConfigManager;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.service.ConfigCenterProcessor;
import com.grace.gateway.core.config.Container;
import com.grace.gateway.register.service.RegisterCenterProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

@Slf4j
public class Bootstrap {

    private Config config;

    private Container container;

    public static void run(String[] args) {
        new Bootstrap().start(args);
    }

    public void start(String[] args) {
        log.info("gateway bootstrap start...");

        // 加载配置
        config = ConfigLoader.load(args);
        log.info("gateway bootstrap load config: {}", config);

        // 初始化配置中心
        initConfigCenter();

        // 启动容器
        initContainer();
        container.start();

        // 初始化注册中心
        initRegisterCenter();

        // 注册钩子，优雅停机
        registerGracefullyShutdown();
    }

    private void initConfigCenter() {
        ConfigCenterProcessor configCenterProcessor = ServiceLoader.load(ConfigCenterProcessor.class).findFirst().orElseThrow(() -> {
            log.error("not found ConfigCenter impl");
            return new RuntimeException("not found ConfigCenter impl");
        });
        configCenterProcessor.init(config.getConfigCenter());
        configCenterProcessor.subscribeRoutesChange(newRoutes -> {
            DynamicConfigManager.getInstance().updateRoutes(newRoutes, true);
            for (RouteDefinition newRoute : newRoutes) {
                DynamicConfigManager.getInstance().changeRoute(newRoute);
            }
        });
    }

    private void initContainer() {
        container = new Container(config);
    }

    private void initRegisterCenter() {
        RegisterCenterProcessor registerCenterProcessor = ServiceLoader.load(RegisterCenterProcessor.class).findFirst().orElseThrow(() -> {
            log.error("not found RegisterCenter impl");
            return new RuntimeException("not found RegisterCenter impl");
        });
        registerCenterProcessor.init(config);
        registerCenterProcessor.subscribeServiceChange(((serviceDefinition, newInstances) -> {
            DynamicConfigManager.getInstance().updateService(serviceDefinition);
            DynamicConfigManager.getInstance().updateInstances(serviceDefinition, newInstances);
        }));
    }

    private void registerGracefullyShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            container.shutdown();
        }));
    }

}
