package com.grace.gateway.config.test;

import cn.hutool.core.bean.BeanUtil;
import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.loader.ConfigLoader;
import com.grace.gateway.config.service.ConfigCenterProcessor;
import com.grace.gateway.config.service.impl.nacos.NacosConfigCenter;
import lombok.Data;
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

    @Test
    public void testCopy() {
        User user1 = new User();
        User user2 = new User();
        user2.setAddress("aaa");
        BeanUtil.copyProperties(user1, user2);
        System.out.println(user1);
        System.out.println(user2);
    }

    @Data
    private class User {
        String name = "zhangsan";
        int age = 23;
        String address;
    }

}
