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
     * 服务是否启用
     */
    private boolean enabled = true;

    public ServiceDefinition(String serviceName) {
        this.serviceName = serviceName;
    }

}
