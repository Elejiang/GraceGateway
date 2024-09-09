package com.grace.gateway.common.constant;

public interface ConfigConstant {

    String CONFIG_PATH = "gateway.yaml"; // 配置文件名称

    String CONFIG_PREFIX = "grace.gateway"; // 配置前缀

    boolean CONFIG_CENTER_DEFAULT_ENABLE = false; // 是否开启配置中心，为了方便起项目，默认关闭

    String CONFIG_NACOS_DEFAULT_NAMESPACE = ""; // 配置中心nacos实现的默认命名空间，为空，代表public

    String CONFIG_NACOS_DEFAULT_DATA_ID = "grace-gateway"; // 配置中心nacos实现的默认Data Id

    String CONFIG_NACOS_DEFAULT_GROUP = "dev"; // 配置中心nacos实现的默认Group

    int CONFIG_NACOS_DEFAULT_TIMEOUT = 5000;  // 配置中心nacos实现的默认超时时长

}
