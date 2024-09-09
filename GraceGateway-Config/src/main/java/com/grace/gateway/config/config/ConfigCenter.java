package com.grace.gateway.config.config;

import com.grace.gateway.common.enums.ConfigCenterEnum;
import com.grace.gateway.config.service.impl.nacos.config.NacosConfig;
import lombok.Data;

import static com.grace.gateway.common.constant.ConfigConstant.CONFIG_CENTER_DEFAULT_ENABLE;

/**
 * 配置中心
 */
@Data
public class ConfigCenter {

    private boolean enable = CONFIG_CENTER_DEFAULT_ENABLE; // 是否开启配置中心

    private ConfigCenterEnum type = ConfigCenterEnum.NACOS; // 配置中心实现

    private String address = "127.0.0.1:8848"; // 配置中心地址

    private NacosConfig nacos = new NacosConfig(); // nacos配置

}
