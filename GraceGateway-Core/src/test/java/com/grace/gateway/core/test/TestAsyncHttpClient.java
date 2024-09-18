package com.grace.gateway.core.test;

import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.loader.ConfigLoader;
import com.grace.gateway.config.manager.DynamicConfigManager;
import com.grace.gateway.core.netty.NettyHttpServer;
import com.grace.gateway.core.netty.processor.NettyCoreProcessor;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.DefaultEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class TestAsyncHttpClient {

    private Config config;

    private AsyncHttpClient asyncHttpClient;

    private NettyHttpServer nettyHttpServer;

    @Before
    public void before() {
        config = ConfigLoader.load(null);
        nettyHttpServer = new NettyHttpServer(config, new NettyCoreProcessor());
        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                .setEventLoopGroup(new DefaultEventLoopGroup()) // 使用传入的Netty事件循环组
                .setConnectTimeout(300000) // 连接超时设置
                .setRequestTimeout(3000) // 请求超时设置
                .setMaxRedirects(100) // 最大重定向次数
                .setAllocator(PooledByteBufAllocator.DEFAULT) // 使用池化的ByteBuf分配器以提升性能
                .setCompressionEnforced(true) // 强制压缩
                .setMaxConnections(10000) // 最大连接数
                .setMaxConnectionsPerHost(10000) // 每个主机的最大连接数
                .setPooledConnectionIdleTimeout(50000); // 连接池中空闲连接的超时时间
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
//        System.out.println(future.get());
        CompletableFuture<Response> responseCompletableFuture = future.toCompletableFuture();
        responseCompletableFuture.whenComplete((response, throwable) -> {
            log.info("{}, \n{}", response, throwable);
        });
        while (true) {}
    }

    @Test
    public void testNettyServer() {
        nettyHttpServer.start();
        DynamicConfigManager.getInstance().updateRoutes(config.getRoutes());
        while(true) {}
    }

}
