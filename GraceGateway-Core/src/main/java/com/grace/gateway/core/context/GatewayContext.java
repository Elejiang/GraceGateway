package com.grace.gateway.core.context;

import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.request.GatewayRequest;
import com.grace.gateway.core.response.GatewayResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class GatewayContext {

    /**
     * Netty上下文
     */
    private ChannelHandlerContext nettyCtx;

    /**
     * 请求过程中发生的异常
     */
    private Throwable throwable;

    private GatewayRequest request;

    private GatewayResponse response;

    private RouteDefinition route;

}
