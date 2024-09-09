package com.grace.gateway.common.enums;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;

@Getter
public enum ResponseCode {

    /* 2xx */
    SUCCESS(HttpResponseStatus.OK, "成功"),


    /* 4xx */
    VERIFICATION_FAILED(HttpResponseStatus.BAD_REQUEST, "请求参数校验失败"),

    PATH_NO_MATCHED(HttpResponseStatus.NOT_FOUND, "没有找到匹配的路径, 请求快速失败"),
    SERVICE_DEFINITION_NOT_FOUND(HttpResponseStatus.NOT_FOUND, "未找到对应的服务定义"),
    SERVICE_INSTANCE_NOT_FOUND(HttpResponseStatus.NOT_FOUND, "未找到对应的服务实例"),

    UNAUTHORIZED(HttpResponseStatus.UNAUTHORIZED, "用户未登陆"),

    BLACKLIST(HttpResponseStatus.FORBIDDEN, "请求IP在黑名单"),
    WHITELIST(HttpResponseStatus.FORBIDDEN, "请求IP不在白名单"),


    /* 5xx */
    SERVICE_UNAVAILABLE(HttpResponseStatus.SERVICE_UNAVAILABLE, "服务暂时不可用,请稍后再试"),

    GATEWAY_FALLBACK(HttpResponseStatus.GATEWAY_TIMEOUT, "请求超时，触发熔断降级"),
    REQUEST_TIMEOUT(HttpResponseStatus.GATEWAY_TIMEOUT, "连接下游服务超时"),

    INTERNAL_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, "网关内部错误"),
    FILTER_CONFIG_PARSE_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, "过滤器配置解析异常"),
    HTTP_RESPONSE_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, "服务返回异常"),
    FLOW_CONTROL_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, "请求过量错误");


    private final HttpResponseStatus status;
    private final String message;

    ResponseCode(HttpResponseStatus status, String msg) {
        this.status = status;
        this.message = msg;
    }

}
