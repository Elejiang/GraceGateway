package com.grace.gateway.core.netty.processor;


import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.helper.GatewayContextHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyCoreProcessor implements NettyProcessor {

    @Override
    public void process(ChannelHandlerContext ctx, FullHttpRequest request) {
        GatewayContext gatewayContext = GatewayContextHelper.buildGatewayContext(request, ctx);
    }

}