package com.grace.gateway.config.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 服务实例
 */
@Data
public class ServiceInstance implements Serializable {

    @Serial
    private static final long serialVersionUID = -7137947815268291319L;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 实例id：ip:port
     */
    private String instanceId;

    /**
     * 服务实例 ip
     */
    private String ip;

    /**
     * 服务实例 port
     */
    private int port;

    /**
     * 权重信息
     */
    private int weight = 1;

    /**
     * 服务实例是否启用
     */
    private boolean enabled = true;

    /**
     * 服务实例是否灰度
     */
    private boolean gray;

    /**
     * 服务实例灰度比例
     */
    private double threshold;

}
