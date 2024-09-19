package com.grace.gateway.core.filter.cors;

import com.grace.gateway.common.enums.ResponseCode;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.Filter;
import com.grace.gateway.core.helper.ContextHelper;
import com.grace.gateway.core.helper.ResponseHelper;
import com.grace.gateway.core.response.GatewayResponse;
import io.netty.handler.codec.http.HttpMethod;

import static com.grace.gateway.common.constant.FilterConstant.CORS_FILTER_NAME;
import static com.grace.gateway.common.constant.FilterConstant.CORS_FILTER_ORDER;

public class CorsFilter implements Filter {

    @Override
    public void doPreFilter(GatewayContext context) {
        if (HttpMethod.OPTIONS.equals(context.getRequest().getMethod())) {
            context.setResponse(ResponseHelper.buildGatewayResponse(ResponseCode.SUCCESS));
            ContextHelper.writeBackResponse(context);
        } else {
            context.doFilter();
        }
    }

    @Override
    public void doPostFilter(GatewayContext context) {
        GatewayResponse gatewayResponse = context.getResponse();
        gatewayResponse.addHeader("Access-Control-Allow-Origin", "*");
        gatewayResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        gatewayResponse.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        gatewayResponse.addHeader("Access-Control-Allow-Credentials", "true");
        context.doFilter();
    }

    @Override
    public String mark() {
        return CORS_FILTER_NAME;
    }

    @Override
    public int getOrder() {
        return CORS_FILTER_ORDER;
    }

}
