package com.grace.gateway.core.netty.processor;


import com.grace.gateway.common.enums.ResponseCode;
import com.grace.gateway.common.exception.GatewayException;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.FilterChainFactory;
import com.grace.gateway.core.helper.ContextHelper;
import com.grace.gateway.core.helper.ResponseHelper;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyCoreProcessor implements NettyProcessor {

    @Override
    public void process(ChannelHandlerContext ctx, FullHttpRequest request) {
        try {
            GatewayContext gatewayContext = ContextHelper.buildGatewayContext(request, ctx);
            FilterChainFactory.buildFilterChain(gatewayContext);

            gatewayContext.doFilter();

        } catch (GatewayException e) {
            log.error("处理错误 {} {}", e.getCode(), e.getCode().getMessage());
            FullHttpResponse httpResponse = ResponseHelper.buildHttpResponse(e.getCode());
            doWriteAndRelease(ctx, request, httpResponse);
        } catch (Throwable t) {
            log.error("处理未知错误", t);
            FullHttpResponse httpResponse = ResponseHelper.buildHttpResponse(ResponseCode.INTERNAL_ERROR);
            doWriteAndRelease(ctx, request, httpResponse);
        }
    }

    private void doWriteAndRelease(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse httpResponse) {
        ctx.writeAndFlush(httpResponse)
                .addListener(ChannelFutureListener.CLOSE); // 发送响应后关闭通道
        ReferenceCountUtil.release(request); // 释放与请求相关联的资源
    }

}