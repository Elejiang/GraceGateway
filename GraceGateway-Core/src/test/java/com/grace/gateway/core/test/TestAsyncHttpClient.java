package com.grace.gateway.core.test;

import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.loader.ConfigLoader;
import com.grace.gateway.core.netty.NettyHttpServer;
import io.netty.buffer.PooledByteBufAllocator;
import org.asynchttpclient.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class TestAsyncHttpClient {

    private Config config;

    private AsyncHttpClient asyncHttpClient;

    private NettyHttpServer nettyHttpServer;

    @Before
    public void before() {
        config = ConfigLoader.load(null);
        nettyHttpServer = new NettyHttpServer(config, null);
        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                .setEventLoopGroup(nettyHttpServer.getEventLoopGroupWorker()) // 使用传入的Netty事件循环组
                .setConnectTimeout(3000) // 连接超时设置
                .setRequestTimeout(3000) // 请求超时设置
                .setMaxRedirects(5) // 最大重定向次数
                .setAllocator(PooledByteBufAllocator.DEFAULT) // 使用池化的ByteBuf分配器以提升性能
                .setCompressionEnforced(true) // 强制压缩
                .setMaxConnections(100) // 最大连接数
                .setMaxConnectionsPerHost(100) // 每个主机的最大连接数
                .setPooledConnectionIdleTimeout(500); // 连接池中空闲连接的超时时间
        // 根据配置创建异步HTTP客户端
        asyncHttpClient = new DefaultAsyncHttpClient(builder.build());
    }

    @Test
    public void testHttp() throws ExecutionException, InterruptedException {
        RequestBuilder builder = new RequestBuilder();
        String url = "http://127.0.0.1:10001/http-server/ping1";
        builder.setMethod("GET");
        builder.setUrl(url);
        ListenableFuture<Response> future = asyncHttpClient.executeRequest(builder.build());
        ListenableFuture<Response> execute = asyncHttpClient.prepareGet(url).execute();
        Response response1 = execute.get();
        System.out.println(response1.toString());
    }

}
