package com.grace.gateway.core.helper;

import com.alibaba.nacos.common.utils.StringUtils;
import com.grace.gateway.config.pojo.ServiceDefinition;
import com.grace.gateway.core.request.GatewayRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.asynchttpclient.Request;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.grace.gateway.common.constant.HttpConstant.HTTP_FORWARD_SEPARATOR;

/**
 * Netty服务端、网关、Http客户端之间的请求转换
 */
public class RequestHelper {

    public static GatewayRequest buildGatewayRequest(ServiceDefinition serviceDefinition, FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        HttpHeaders headers = fullHttpRequest.headers(); // 服务端的http请求头
        String host = headers.get(HttpHeaderNames.HOST); // host
        HttpMethod method = fullHttpRequest.method(); // http请求类型
        String uri = fullHttpRequest.uri(); // uri
        String clientIp = getClientIp(ctx, fullHttpRequest); // 客户端ip
        String contentType = HttpUtil.getMimeType(fullHttpRequest) == null ? null :
                HttpUtil.getMimeType(fullHttpRequest).toString(); // 请求的MIME类型
        Charset charset = HttpUtil.getCharset(fullHttpRequest, StandardCharsets.UTF_8); // 字符集

        return new GatewayRequest(serviceDefinition, charset, clientIp, host, uri, method,
                contentType, headers, fullHttpRequest);
    }

    public static Request buildHttpClientRequest(GatewayRequest gatewayRequest) {
        return gatewayRequest.build();
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
