package com.grace.gateway.core.context;

import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.filter.FilterChain;
import com.grace.gateway.core.helper.ContextHelper;
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

    private boolean keepAlive;

    private FilterChain filterChain;

    private int curFilterIndex = 0;
    private boolean isDoPreFilter = true;

    public GatewayContext(ChannelHandlerContext nettyCtx, GatewayRequest request,
                          RouteDefinition route, boolean keepAlive) {
        this.nettyCtx = nettyCtx;
        this.request = request;
        this.route = route;
        this.keepAlive = keepAlive;
    }

    public void doFilter() {
        int size = filterChain.size();
        if (isDoPreFilter) {
            filterChain.doPreFilter(curFilterIndex++, this);
            if (curFilterIndex == size) {
                isDoPreFilter = false;
                curFilterIndex--;
            }
        } else {
            filterChain.doPostFilter(curFilterIndex--, this);
            if (curFilterIndex < 0) {
                ContextHelper.writeBackResponse(this);
            }
        }
    }

}
