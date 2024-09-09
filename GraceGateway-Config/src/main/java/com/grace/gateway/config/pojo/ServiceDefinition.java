package com.grace.gateway.config.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 服务定义
 */
@Data
public class ServiceDefinition implements Serializable {

    @Serial
    private static final long serialVersionUID = -3751925283314024046L;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 版本号
     */
    private String version;

    /**
     * 环境
     */
    private String envType;

    /**
     * 服务是否启用
     */
    private boolean enable = true;

}
