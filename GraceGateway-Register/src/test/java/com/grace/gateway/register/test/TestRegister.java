package com.grace.gateway.register.test;

import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.loader.ConfigLoader;
import com.grace.gateway.register.service.impl.nacos.NacosRegisterCenter;
import org.junit.Test;

public class TestRegister {

    @Test
    public void testRegister() {
        Config config = ConfigLoader.load(null);
        NacosRegisterCenter center = new NacosRegisterCenter();
        center.init(config);
        while(true) {}
    }

    @Test
    public void testRegisterSub() {
        Config config = ConfigLoader.load(null);
        NacosRegisterCenter center = new NacosRegisterCenter();
        center.init(config);
        center.subscribeServiceChange(((serviceDefinition, newInstances) -> {
            System.out.println(serviceDefinition);
            System.out.println(newInstances);
        }));
        while(true) {}
    }

}
