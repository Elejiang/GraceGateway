package com.grace.gateway.core.response;


import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;
import org.asynchttpclient.Response;



@Data
public class GatewayResponse {
    /**
     * 响应头
     */
    private HttpHeaders responseHeaders = new DefaultHttpHeaders();
    /**
     * 响应内容
     */
    private String content;
    /**
     * 响应返回码
     */
    private HttpResponseStatus httpResponseStatus;
    /**
     * 响应结果
     */
    private Response response;

    /**
     * 设置响应头信息
     */
    public void addHeader(CharSequence key, CharSequence val) {
        responseHeaders.add(key, val);
    }

}
