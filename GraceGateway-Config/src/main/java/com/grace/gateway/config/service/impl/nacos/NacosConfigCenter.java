package com.grace.gateway.config.service.impl.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grace.gateway.config.config.ConfigCenter;
import com.grace.gateway.config.service.ConfigCenterProcessor;
import com.grace.gateway.config.service.RoutesChangeListener;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Properties;

public class NacosConfigCenter implements ConfigCenterProcessor {

    /**
     * 配置中心信息
     */
    private ConfigCenter configCenter;

    /**
     * Nacos提供的与配置中心进行交互的接口
     */
    private ConfigService configService;


    @SneakyThrows(NacosException.class)
    @Override
    public void init(ConfigCenter configCenter) {
        this.configCenter = configCenter;
        Properties properties = buildProperties(configCenter);
        this.configService = NacosFactory.createConfigService(properties);
    }

    @Override
    public void subscribeRoutesChange(RoutesChangeListener listener) {

    }

    private Properties buildProperties(ConfigCenter configCenter) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, configCenter.getConfigAddress());
        ObjectMapper mapper = new ObjectMapper();
        properties.putAll(mapper.convertValue(configCenter.getNacosConfig(), Map.class));
        return properties;
    }

}
