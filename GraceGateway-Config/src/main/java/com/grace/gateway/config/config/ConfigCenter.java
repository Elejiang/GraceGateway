package com.grace.gateway.config.config;

import com.grace.gateway.common.enums.ConfigCenterEnum;
import com.grace.gateway.config.service.impl.nacos.config.NacosConfig;
import lombok.Data;

/**
 * 配置中心
 */
@Data
public class ConfigCenter {

    private ConfigCenterEnum type = ConfigCenterEnum.NACOS; // 配置中心实现

    private String configAddress = "127.0.0.1:8848"; // 配置中心地址

    private NacosConfig nacosConfig = new NacosConfig(); // nacos配置

}
