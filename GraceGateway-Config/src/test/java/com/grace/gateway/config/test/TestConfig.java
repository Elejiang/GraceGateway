package com.grace.gateway.config.test;

import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.util.ConfigUtil;
import org.junit.Test;



public class TestConfig {

    @Test
    public void testConfigLoad() {
        Config config = ConfigUtil.loadConfigFromYaml("gateway.yaml", Config.class, "grace.gateway");
        System.out.println(config);
    }

}
