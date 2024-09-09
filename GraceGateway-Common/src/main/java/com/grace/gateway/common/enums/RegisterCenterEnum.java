package com.grace.gateway.common.enums;

import lombok.Getter;

@Getter
public enum RegisterCenterEnum {

    NACOS("nacos"),
    ZOOKEEPER("zookeeper");

    private final String des;

    RegisterCenterEnum(String des) {
        this.des = des;
    }

}
