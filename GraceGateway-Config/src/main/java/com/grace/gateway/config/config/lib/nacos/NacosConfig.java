package com.grace.gateway.config.config.lib.nacos;

import lombok.Data;

import static com.grace.gateway.common.constant.NacosConstant.*;

@Data
public class NacosConfig {

    /**
     * 命名空间，是命名空间id，并非名字
     */
    private String namespace = NACOS_DEFAULT_NAMESPACE;

    /**
     * nacos配置的 Data Id
     */
    private String dataId = NACOS_DEFAULT_DATA_ID;

    /**
     * nacos配置的 Group
     */
    private String group = NACOS_DEFAULT_GROUP;

    /**
     * nacos连接超时时长，单位ms
     */
    private int timeout = NACOS_DEFAULT_TIMEOUT;

}
