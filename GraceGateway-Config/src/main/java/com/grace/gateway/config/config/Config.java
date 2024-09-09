package com.grace.gateway.config.config;

import com.grace.gateway.common.enums.RegisterCenterEnum;
import com.grace.gateway.config.pojo.RouteDefinition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关静态配置
 */
@Data
public class Config {

    // base
    private int port = 9999;
    private String env = "dev";

    // 配置中心
    private ConfigCenter configCenter = new ConfigCenter();

    // 注册中心
    private RegisterCenter registerCenter = new RegisterCenter();

    //netty
    private NettyConfig netty = new NettyConfig();

    // 路由配置
    private List<RouteDefinition> routes = new ArrayList<>();

}
