package com.grace.gateway.core.netty.processor;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface NettyProcessor {

    void process(ChannelHandlerContext ctx, FullHttpRequest request);

}
