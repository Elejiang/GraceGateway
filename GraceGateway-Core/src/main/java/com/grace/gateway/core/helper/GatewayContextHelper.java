package com.grace.gateway.core.helper;

import com.alibaba.nacos.common.utils.StringUtils;
import com.grace.gateway.config.helper.RouteResolver;
import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.request.GatewayRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.grace.gateway.common.constant.HttpConstant.HTTP_FORWARD_SEPARATOR;

public class GatewayContextHelper {

    public static GatewayContext buildGatewayContext(FullHttpRequest request, ChannelHandlerContext ctx) {
        RouteDefinition route = RouteResolver.matchingRouteByUri(request.uri());

        GatewayRequest gatewayRequest = buildGatewayRequest(route.getServiceName(), request, ctx);

        return new GatewayContext(ctx, gatewayRequest, route, HttpUtil.isKeepAlive(request));
    }

    /**
     * 构建Request请求对象
     */
    private static GatewayRequest buildGatewayRequest(String serviceName, FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        HttpHeaders headers = fullHttpRequest.headers();
        String host = headers.get(HttpHeaderNames.HOST);
        HttpMethod method = fullHttpRequest.method();
        String uri = fullHttpRequest.uri();
        String clientIp = getClientIp(ctx, fullHttpRequest);
        String contentType = HttpUtil.getMimeType(fullHttpRequest) == null ? null :
                HttpUtil.getMimeType(fullHttpRequest).toString();
        Charset charset = HttpUtil.getCharset(fullHttpRequest, StandardCharsets.UTF_8);

        return new GatewayRequest(serviceName, charset, clientIp, host, uri, method,
                contentType, headers, fullHttpRequest);
    }

    private static String getClientIp(ChannelHandlerContext ctx, FullHttpRequest request) {
        String xForwardedValue = request.headers().get(HTTP_FORWARD_SEPARATOR);

        String clientIp = null;
        if (StringUtils.isNotEmpty(xForwardedValue)) {
            List<String> values = Arrays.asList(xForwardedValue.split(", "));
            if (values.size() >= 1 && StringUtils.isNotBlank(values.get(0))) {
                clientIp = values.get(0);
            }
        }
        if (clientIp == null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            clientIp = inetSocketAddress.getAddress().getHostAddress();
        }
        return clientIp;
    }


}
