package com.grace.gateway.config.service.impl.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grace.gateway.config.config.ConfigCenter;
import com.grace.gateway.config.service.ConfigCenterProcessor;
import com.grace.gateway.config.service.RoutesChangeListener;
import com.grace.gateway.config.service.impl.nacos.config.NacosConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class NacosConfigCenter implements ConfigCenterProcessor {

    /**
     * 配置中心信息
     */
    private ConfigCenter configCenter;

    /**
     * Nacos提供的与配置中心进行交互的接口
     */
    private ConfigService configService;

    /**
     * 是否完成初始化
     */
    private final AtomicBoolean init = new AtomicBoolean(false);

    public NacosConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
        init(configCenter);
    }

    @SneakyThrows(NacosException.class)
    @Override
    public void init(ConfigCenter configCenter) {
        if (!configCenter.isEnable() || !init.compareAndSet(false, true)) {
            return;
        }
        this.configService = NacosFactory.createConfigService(buildProperties(configCenter));
    }

    @SneakyThrows(NacosException.class)
    @Override
    public void subscribeRoutesChange(RoutesChangeListener listener) {
        if (!configCenter.isEnable()) {
            return;
        }
        NacosConfig nacos = configCenter.getNacos();
        String configJson = configService.getConfig(nacos.getDataId(), nacos.getGroup(), nacos.getTimeout());
        log.info("config from nacos: {}", configJson);

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
