package com.grace.gateway.core.filter.route;

import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.filter.Filter;
import com.grace.gateway.core.helper.ContextHelper;
import com.grace.gateway.core.helper.ResponseHelper;
import com.grace.gateway.core.http.HttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;


import java.util.concurrent.CompletableFuture;

import static com.grace.gateway.common.constant.FilterConstant.ROUTE_FILTER_NAME;
import static com.grace.gateway.common.constant.FilterConstant.ROUTE_FILTER_ORDER;

public class RouteFilter implements Filter {

    @Override
    public void doPreFilter(GatewayContext context) {
        RouteDefinition.ResilienceConfig resilience = context.getRoute().getResilience();
        if (resilience.isEnabled()) { // 开启弹性配置

        } else {
            executeRoute(context);
        }

    }

    public void executeRoute(GatewayContext context) {
        Request request = context.getRequest().build();
        CompletableFuture<Response> future = HttpClient.getInstance().executeRequest(request);
        future.whenComplete(((response, throwable) -> {
            if (throwable != null) {
                context.setThrowable(throwable);
                throw new RuntimeException(throwable);
            }
            context.setResponse(ResponseHelper.buildGatewayResponse(response));
            context.getFilterChain().doPostFilter(context); // 过滤器后处理
            ContextHelper.writeBackResponse(context); // 写回数据
        }));
    }

    @Override
    public void doPostFilter(GatewayContext context) {

    }

    @Override
    public String mark() {
        return ROUTE_FILTER_NAME;
    }

    @Override
    public int getOrder() {
        return ROUTE_FILTER_ORDER;
    }

}
