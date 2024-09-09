package com.grace.gateway.common.exception;

import com.grace.gateway.common.enums.ResponseCode;
import lombok.Getter;

import java.io.Serial;

@Getter
public class GatewayException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1159027826621990252L;
    protected ResponseCode code;

    public GatewayException() {
    }

    public GatewayException(String message, ResponseCode code) {
        super(message);
        this.code = code;
    }

    public GatewayException(String message, Throwable cause, ResponseCode code) {
        super(message, cause);
        this.code = code;
    }

}
