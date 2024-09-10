package com.grace.gateway.config.service.impl.nacos;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grace.gateway.config.config.ConfigCenter;
import com.grace.gateway.config.config.lib.nacos.NacosConfig;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.service.AbstractConfigCenterProcessor;
import com.grace.gateway.config.service.RoutesChangeListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

@Slf4j
public class NacosConfigCenter extends AbstractConfigCenterProcessor {

    /**
     * Nacos提供的与配置中心进行交互的接口
     */
    private ConfigService configService;

    public NacosConfigCenter(ConfigCenter configCenter) {
        super(configCenter);
    }

    @SneakyThrows(NacosException.class)
    protected void initialize() {
        this.configService = NacosFactory.createConfigService(buildProperties(configCenter));
    }

    @SneakyThrows(NacosException.class)
    protected void subscribeConfigChange(RoutesChangeListener listener) {
        NacosConfig nacos = configCenter.getNacos();
        String configJson = configService.getConfig(nacos.getDataId(), nacos.getGroup(), nacos.getTimeout());
        /* configJson:
         * {
         *     "routes": [
         *         {
         *             "id": "test1",
         *             "serviceName": "user"
         *         },
         *         {
         *             "id": "test2",
         *             "serviceName": "order"
         *         }
         *     ]
         * }
         */
        log.info("config from nacos: {}", configJson);
        List<RouteDefinition> routes = JSON.parseObject(configJson).getJSONArray("routes").toJavaList(RouteDefinition.class);
        listener.onRoutesChange(routes);

        configService.addListener(nacos.getDataId(), nacos.getGroup(), new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("config change from nacos: {}", configInfo);
                List<RouteDefinition> routes = JSON.parseObject(configJson).getJSONArray("routes").toJavaList(RouteDefinition.class);
                listener.onRoutesChange(routes);
            }
        });


    }

    private Properties buildProperties(ConfigCenter configCenter) {
        ObjectMapper mapper = new ObjectMapper();
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, configCenter.getAddress());
        Map map = mapper.convertValue(configCenter.getNacos(), Map.class);
        if (map == null || map.isEmpty()) return properties;
        properties.putAll(map);
        return properties;
    }

}
