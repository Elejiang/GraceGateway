package com.grace.gateway.common.exception;


import com.grace.gateway.common.enums.ResponseCode;

import java.io.Serial;

/**
 * 当下游服务找不到时抛出该异常
 */
public class NotFoundException extends GatewayException {

    @Serial
	private static final long serialVersionUID = -4825153388389722853L;

    public NotFoundException(ResponseCode code) {
        super(code.getMessage(), code);
    }

    public NotFoundException(Throwable cause, ResponseCode code) {
        super(code.getMessage(), cause, code);
    }

}
