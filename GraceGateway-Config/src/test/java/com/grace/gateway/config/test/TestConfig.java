package com.grace.gateway.config.test;

import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.loader.ConfigLoader;
import com.grace.gateway.config.service.ConfigCenterProcessor;
import com.grace.gateway.config.service.impl.nacos.NacosConfigCenter;
import org.junit.Before;
import org.junit.Test;



public class TestConfig {
    Config config;

    @Before
    public void before() {
        this.config = ConfigLoader.load(null);
    }

    @Test
    public void testConfigLoad() {
        System.out.println(config);
    }

    @Test
    public void testNacosConfig() {
        ConfigCenterProcessor processor = new NacosConfigCenter();
        processor.init(config.getConfigCenter());
        processor.subscribeRoutesChange(i -> {});
    }

}
