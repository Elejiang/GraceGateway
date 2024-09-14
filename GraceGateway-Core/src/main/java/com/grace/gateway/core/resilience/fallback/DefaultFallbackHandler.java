package com.grace.gateway.core.resilience.fallback;

import com.grace.gateway.common.enums.ResponseCode;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.helper.ContextHelper;
import com.grace.gateway.core.helper.ResponseHelper;

import static com.grace.gateway.common.constant.FallbackConstant.DEFAULT_FALLBACK_HANDLER_NAME;

public class DefaultFallbackHandler implements FallbackHandler {

    @Override
    public void handle(Throwable throwable, GatewayContext context) {
        context.setThrowable(throwable);
        context.setResponse(ResponseHelper.buildGatewayResponse(ResponseCode.GATEWAY_FALLBACK));
        ContextHelper.writeBackResponse(context);
    }

    @Override
    public String mark() {
        return DEFAULT_FALLBACK_HANDLER_NAME;
    }

}
