package com.grace.gateway.config.service.impl.nacos;

import lombok.Data;

import static com.grace.gateway.common.constant.ConfigConstant.*;

@Data
public class NacosConfig {

    /**
     * 命名空间，是命名空间id，并非名字
     */
    private String namespace = CONFIG_NACOS_DEFAULT_NAMESPACE;

    /**
     * nacos配置的 Data Id
     */
    private String dataId = CONFIG_NACOS_DEFAULT_DATA_ID;

    /**
     * nacos配置的 Group
     */
    private String group = CONFIG_NACOS_DEFAULT_GROUP;

    /**
     * nacos连接超时时长，单位ms
     */
    private int timeout = CONFIG_NACOS_DEFAULT_TIMEOUT;

}
