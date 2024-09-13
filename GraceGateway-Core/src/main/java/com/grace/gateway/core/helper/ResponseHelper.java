package com.grace.gateway.core.helper;


import cn.hutool.json.JSONUtil;
import com.grace.gateway.common.enums.ResponseCode;
import com.grace.gateway.core.response.GatewayResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;

import java.util.Objects;


/**
 * Netty服务端、网关、Http客户端之间的响应转换
 */
@Slf4j
public class ResponseHelper {

    public static FullHttpResponse buildHttpResponse(GatewayResponse gatewayResponse) {
        ByteBuf content;
        if (Objects.nonNull(gatewayResponse.getResponse())) {
            content = Unpooled.wrappedBuffer(gatewayResponse.getResponse().getResponseBodyAsByteBuffer()); // 下游服务的http响应结果
        } else if (gatewayResponse.getContent() != null) {
            content = Unpooled.wrappedBuffer(gatewayResponse.getContent().getBytes());
        } else {
            content = Unpooled.wrappedBuffer("".getBytes());
        }

        DefaultFullHttpResponse httpResponse;
        if (Objects.nonNull(gatewayResponse.getResponse())) { // 下游响应不为空，直接拿下游响应构造
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.valueOf(gatewayResponse.getResponse().getStatusCode()), content);
            httpResponse.headers().add(gatewayResponse.getResponse().getHeaders()); //
        } else {
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    gatewayResponse.getHttpResponseStatus(), content);
            httpResponse.headers().add(gatewayResponse.getResponseHeaders());
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
        }

        return httpResponse;
    }

    public static FullHttpResponse buildHttpResponse(ResponseCode responseCode) {
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                responseCode.getStatus(),
                Unpooled.wrappedBuffer(responseCode.getMessage().getBytes()));
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());

        return httpResponse;
    }


    public static GatewayResponse buildGatewayResponse(Response response) {
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setResponseHeaders(response.getHeaders());
        gatewayResponse.setHttpResponseStatus(HttpResponseStatus.valueOf(response.getStatusCode()));
        gatewayResponse.setContent(response.getResponseBody());
        gatewayResponse.setResponse(response);

        return gatewayResponse;
    }

    public static GatewayResponse buildGatewayResponse(ResponseCode code) {
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.addHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        gatewayResponse.setHttpResponseStatus(code.getStatus());
        gatewayResponse.setContent(JSONUtil.toJsonStr(code.getMessage()));

        return gatewayResponse;
    }

    public static GatewayResponse buildGatewayResponse(Object data) {
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.addHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        gatewayResponse.setHttpResponseStatus(ResponseCode.SUCCESS.getStatus());
        gatewayResponse.setContent(JSONUtil.toJsonStr(data));

        return gatewayResponse;
    }
}
