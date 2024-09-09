package com.grace.gateway.common.exception;


import com.grace.gateway.common.enums.ResponseCode;

import java.io.Serial;

/**
 * 限制异常，一般发生在流控
 */
public class LimitedException extends GatewayException {

    @Serial
    private static final long serialVersionUID = -5975157585816767314L;

    public LimitedException(ResponseCode code) {
        super(code.getMessage(), code);
    }

    public LimitedException(Throwable cause, ResponseCode code) {
        super(code.getMessage(), cause, code);
    }

}
