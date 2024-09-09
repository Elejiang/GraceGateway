package com.grace.gateway.common.exception;

import com.grace.gateway.common.enums.ResponseCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 连接异常，连接某实例时发生异常
 */
@Getter
public class ConnectException extends GatewayException {

    @Serial
    private static final long serialVersionUID = 3804307918593460840L;

    private final String instanceId;

    private final String requestUrl;


    public ConnectException(String instanceId, String requestUrl) {
        this.instanceId = instanceId;
        this.requestUrl = requestUrl;
    }

    public ConnectException(Throwable cause, String instanceId, String requestUrl, ResponseCode code) {
        super(code.getMessage(), cause, code);
        this.instanceId = instanceId;
        this.requestUrl = requestUrl;
    }

}
