package com.grace.gateway.core.request;

import com.alibaba.nacos.common.utils.StringUtils;
import com.grace.gateway.common.constant.HttpConstant;
import com.grace.gateway.config.pojo.ServiceDefinition;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Data;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.grace.gateway.common.constant.BasicConstant.DATE_DEFAULT_FORMATTER;


/**
 * 网关请求
 */
@Data
public class GatewayRequest {

    /**
     * 请求流水号
     */
    private final String id;

    /**
     * 服务名
     */
    private final ServiceDefinition serviceDefinition;

    /**
     * 请求进入网关时间
     */
    private final long beginTime;

    /**
     * 字符集
     */
    private final Charset charset;

    /**
     * 客户端的IP，主要用于做流控、黑白名单
     */
    private final String clientIp;

    /**
     * 请求的地址：IP:port
     */
    private final String host;

    /**
     * 请求的路径   /XXX/XXX/XX
     */
    private final String path;

    /**
     * URI：统一资源标识符，/XXX/XXX/XXX?attr1=value&attr2=value2
     */
    private final String uri;

    /**
     * 请求方法 POST/PUT/GET
     */
    private final HttpMethod method;

    /**
     * 请求的格式
     */
    private final String contentType;

    /**
     * 请求头信息
     */
    private final HttpHeaders headers;

    /**
     * 参数解析器
     */
    private final QueryStringDecoder queryStringDecoder;

    /**
     * FullHttpRequest
     */
    private final FullHttpRequest fullHttpRequest;

    /**
     * 构建下游请求
     */
    private final RequestBuilder requestBuilder;

    /**
     * 请求体
     */
    private String body;

    /**
     * 请求Cookie
     */
    private Map<String, io.netty.handler.codec.http.cookie.Cookie> cookieMap;

    /**
     * post请求定义的参数结合
     */
    private Map<String, List<String>> postParameters;

    /**
     * 发给下游的scheme，默认是http://
     */
    private String modifyScheme;

    /**
     * 发给下游的host
     */
    private String modifyHost;

    /**
     * 发给下游的path
     */
    private String modifyPath;

    /**
     * 是否灰度
     */
    private boolean isGray;

    public GatewayRequest(ServiceDefinition serviceDefinition, Charset charset, String clientIp, String host, String uri, HttpMethod method, String contentType, HttpHeaders headers, FullHttpRequest fullHttpRequest) {
        this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_DEFAULT_FORMATTER)) + "---" + UUID.randomUUID();
        this.serviceDefinition = serviceDefinition;
        this.beginTime = System.currentTimeMillis();
        this.charset = charset;
        this.clientIp = clientIp;
        this.host = host;
        this.uri = uri;
        this.method = method;
        this.contentType = contentType;
        this.headers = headers;
        this.fullHttpRequest = fullHttpRequest;

        this.queryStringDecoder = new QueryStringDecoder(uri, charset);
        this.path = queryStringDecoder.path();
        this.modifyHost = host;
        this.modifyPath = path;
        this.modifyScheme = HttpConstant.HTTP_PREFIX_SEPARATOR;

        this.requestBuilder = new RequestBuilder();
        this.requestBuilder.setMethod(method.name());
        this.requestBuilder.setHeaders(headers);
        this.requestBuilder.setQueryParams(queryStringDecoder.parameters());
        ByteBuf contentBuffer = fullHttpRequest.content();
        if (Objects.nonNull(contentBuffer)) {
            this.requestBuilder.setBody(contentBuffer.nioBuffer());
            contentBuffer.release();
        }
    }


    /**
     * 获取Cookie
     */
    public io.netty.handler.codec.http.cookie.Cookie getCookie(String name) {
        if (cookieMap == null) {
            cookieMap = new HashMap<>();
            String cookieStr = headers.get(HttpHeaderNames.COOKIE);
            if (StringUtils.isBlank(cookieStr)) {
                return null;
            }
            Set<io.netty.handler.codec.http.cookie.Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieStr);
            for (io.netty.handler.codec.http.cookie.Cookie cookie : cookies) {
                cookieMap.put(name, cookie);
            }
        }
        return cookieMap.get(name);
    }

    public Request build() {
        return requestBuilder.setUrl(modifyScheme + modifyHost + modifyPath).build();
    }

}
