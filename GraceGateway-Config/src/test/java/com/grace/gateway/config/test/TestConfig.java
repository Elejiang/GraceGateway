package com.grace.gateway.config.test;

import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.service.ConfigCenterProcessor;
import com.grace.gateway.config.service.impl.nacos.NacosConfigCenter;
import com.grace.gateway.config.util.ConfigUtil;
import org.junit.Before;
import org.junit.Test;

import static com.grace.gateway.common.constant.ConfigConstant.CONFIG_PATH;
import static com.grace.gateway.common.constant.ConfigConstant.CONFIG_PREFIX;


public class TestConfig {
    Config config;

    @Before
    public void before() {
        this.config = ConfigUtil.loadConfigFromYaml(CONFIG_PATH, Config.class, CONFIG_PREFIX);
    }

    @Test
    public void testConfigLoad() {
        System.out.println(config);
    }

    @Test
    public void testNacosConfig() {
        ConfigCenterProcessor processor = new NacosConfigCenter(config.getConfigCenter());
        processor.subscribeRoutesChange(i -> {});
    }

}
