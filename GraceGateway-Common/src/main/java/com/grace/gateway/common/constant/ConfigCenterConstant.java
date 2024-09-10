package com.grace.gateway.common.constant;

import com.grace.gateway.common.enums.ConfigCenterEnum;

public interface ConfigCenterConstant {

    boolean CONFIG_CENTER_DEFAULT_ENABLED = false; // 是否开启配置中心，为了方便起项目，默认关闭

    ConfigCenterEnum CONFIG_CENTER_DEFAULT_IMPL = ConfigCenterEnum.NACOS; // 配置中心默认实现

    String CONFIG_CENTER_DEFAULT_ADDRESS = "127.0.0.1:8848"; // 配置中心默认地址

}
