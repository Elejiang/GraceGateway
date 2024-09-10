package com.grace.gateway.core.response;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.grace.gateway.common.enums.ResponseCode;
import io.netty.handler.codec.http.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.asynchttpclient.Response;

import static com.grace.gateway.common.constant.HttpConstant.*;


@Data
@NoArgsConstructor
public class GatewayResponse {
    /**
     * 额外的响应结果
     */
    private final HttpHeaders extraResponseHeaders = new DefaultHttpHeaders();
    /**
     * 响应头
     */
    private HttpHeaders responseHeaders = new DefaultHttpHeaders();
    /**
     * 响应内容
     */
    private String content;
    /**
     * 异步返回对象
     */
    private Response futureResponse;
    /**
     * 响应返回码
     */
    private HttpResponseStatus httpResponseStatus;

    /**
     * 构建异步响应对象
     */
    public static GatewayResponse buildGatewayResponse(Response futureResponse) {
        GatewayResponse response = new GatewayResponse();
        response.setFutureResponse(futureResponse);
        response.setHttpResponseStatus(HttpResponseStatus.valueOf(futureResponse.getStatusCode()));
        return response;
    }

    /**
     * 处理返回json对象，失败时调用
     */
    @SneakyThrows
    public static GatewayResponse buildGatewayResponse(ResponseCode code) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put(STATUS, code.getStatus().code());
        objectNode.put(MESSAGE, code.getMessage());

        GatewayResponse response = new GatewayResponse();
        response.setHttpResponseStatus(code.getStatus());
        response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        response.setContent(mapper.writeValueAsString(objectNode));

        return response;
    }

    /**
     * 处理返回json对象，成功时调用
     */
    @SneakyThrows
    public static GatewayResponse buildGatewayResponse(Object data) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();

        if (data instanceof ResponseCode code) {
            objectNode.put(STATUS, code.getStatus().code());
            objectNode.putPOJO(DATA, code.getMessage());
        } else {
            objectNode.put(STATUS, ResponseCode.SUCCESS.getStatus().code());
            objectNode.putPOJO(DATA, data);
        }

        GatewayResponse response = new GatewayResponse();
        response.setHttpResponseStatus(ResponseCode.SUCCESS.getStatus());
        response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        response.setContent(mapper.writeValueAsString(objectNode));
        return response;
    }

    /**
     * 设置响应头信息
     */
    public void putHeader(CharSequence key, CharSequence val) {
        responseHeaders.add(key, val);
    }

}
