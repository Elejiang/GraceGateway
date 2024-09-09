package com.grace.gateway.config.config;

import com.grace.gateway.common.enums.RegisterCenterEnum;
import lombok.Data;

/**
 * 注册中心
 */
@Data
public class RegisterCenter {

    private RegisterCenterEnum registerCenter = RegisterCenterEnum.NACOS; // 注册中心实现

    private String registerAddress = "127.0.0.1:8848"; // 注册中心地址

}
