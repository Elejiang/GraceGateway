package com.grace.gateway.core.helper;


import cn.hutool.json.JSONUtil;
import com.grace.gateway.common.enums.ResponseCode;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.response.GatewayResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 构建netty的http返回结果
 */
@Slf4j
public class ResponseHelper {

    private static final double DEFAULT_ALPHA = 0.2; // 默认的 alpha 值

    // 发送成功的数据包数量
    private static final AtomicInteger successCount = new AtomicInteger(0);
    // 发送失败的数据包数量
    private static final AtomicInteger failureCount = new AtomicInteger(0);
    private static final double alpha = DEFAULT_ALPHA; // 指数加权移动平均的平滑因子
    private static double lossRate = 0.0; // 初始丢包率

    /**
     * 获取响应对象，失败时调用
     */
    public static FullHttpResponse getHttpResponse(ResponseCode responseCode) {
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                responseCode.getStatus(),
                Unpooled.wrappedBuffer(responseCode.getMessage().getBytes()));
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
        return httpResponse;
    }

    /**
     * 写回响应信息方法
     */
    public static void writeResponse(GatewayContext context) {
        FullHttpResponse httpResponse = getHttpResponse(context.getResponse());

        if (!context.isKeepAlive()) {
            context.getNettyCtx()
                    .writeAndFlush(httpResponse)
                    .addListener(ChannelFutureListener.CLOSE)
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            // 发送成功
                            successCount.incrementAndGet();
                        } else {
                            // 发送失败
                            failureCount.incrementAndGet();
                        }
                    });
        } else {
            httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            context.getNettyCtx()
                    .writeAndFlush(httpResponse)
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            // 发送成功
                            successCount.incrementAndGet();
                        } else {
                            // 发送失败
                            failureCount.incrementAndGet();
                        }
                    });

        }
        updateLossRate();

        log.info("当前丢包率为: {}", lossRate);
    }

    /**
     * 通过我们自己的Response对象 构建Netty的FullHttpResponse
     */
    private static FullHttpResponse getHttpResponse(GatewayResponse gatewayResponse) {
        ByteBuf content;
        if (Objects.nonNull(gatewayResponse.getResponse())) {
            content = Unpooled.wrappedBuffer(gatewayResponse.getResponse().getResponseBodyAsByteBuffer());
        } else if (gatewayResponse.getContent() != null) {
            content = Unpooled.wrappedBuffer(gatewayResponse.getContent().getBytes());
        } else {
            content = Unpooled.wrappedBuffer("".getBytes());
        }

        DefaultFullHttpResponse httpResponse;
        if (Objects.nonNull(gatewayResponse.getResponse())) {
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.valueOf(gatewayResponse.getResponse().getStatusCode()), content);
            httpResponse.headers().add(gatewayResponse.getResponse().getHeaders());
        } else {
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    gatewayResponse.getHttpResponseStatus(), content);
            httpResponse.headers().add(gatewayResponse.getResponseHeaders());
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
        }
        return httpResponse;
    }

    /**
     * 使用指数加权移动平均（Exponential Weighted Moving Average，EWMA）算法。
     * 这是一种在时间序列数据中广泛应用的算法，它对最近的观测值赋予更高的权重，
     * 并对较早的观测值赋予较低的权重。这样，就可以更准确地反映出丢包率的变化趋势，
     * 而不仅仅是简单地计算成功和失败的比例。
     */
    private static void updateLossRate() {
        int success = successCount.get();
        int failure = failureCount.get();
        int total = success + failure;
        if (total != 0) {
            double newLossRate = alpha * failure / total;
            lossRate = alpha * newLossRate + (1 - alpha) * lossRate;
        }
    }


    public static GatewayResponse buildGatewayResponse(Response response) {
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setHttpResponseStatus(HttpResponseStatus.valueOf(response.getStatusCode()));
        gatewayResponse.setContent(response.getResponseBody());
        gatewayResponse.setResponse(response);
        return gatewayResponse;
    }

    /**
     * 处理返回json对象，失败时调用
     */
    @SneakyThrows
    public static GatewayResponse buildGatewayResponse(ResponseCode code) {
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.addHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        gatewayResponse.setHttpResponseStatus(code.getStatus());
        gatewayResponse.setContent(JSONUtil.toJsonStr(code.getMessage()));

        return gatewayResponse;
    }

    /**
     * 处理返回json对象，成功时调用
     */
    @SneakyThrows
    public static GatewayResponse buildGatewayResponse(Object data) {
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.addHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        gatewayResponse.setHttpResponseStatus(ResponseCode.SUCCESS.getStatus());
        gatewayResponse.setContent(JSONUtil.toJsonStr(data));

        return gatewayResponse;
    }
}
