package com.grace.gateway.common.exception;


import com.grace.gateway.common.enums.ResponseCode;

import java.io.Serial;

public class ResponseException extends GatewayException {

    @Serial
    private static final long serialVersionUID = 707018357827678269L;

    public ResponseException(ResponseCode code) {
        super(code.getMessage(), code);
    }

    public ResponseException(Throwable cause, ResponseCode code) {
        super(code.getMessage(), cause, code);
        this.code = code;
    }

}
