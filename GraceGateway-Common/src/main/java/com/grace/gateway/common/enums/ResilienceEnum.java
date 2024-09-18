package com.grace.gateway.common.enums;

public enum ResilienceEnum {

    RETRY("重试"),
    CIRCUITBREAKER("熔断"),
    FALLBACK("降级"),
    BULKHEAD("信号量隔离"),
    THREADPOOLBULKHEAD("线程池隔离")
    ;


    private final String des;

    ResilienceEnum(String des) {
        this.des = des;
    }
}
