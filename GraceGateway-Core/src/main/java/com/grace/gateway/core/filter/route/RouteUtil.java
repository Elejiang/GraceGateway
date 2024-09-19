package com.grace.gateway.core.filter.route;

import com.grace.gateway.core.context.GatewayContext;
import com.grace.gateway.core.helper.ResponseHelper;
import com.grace.gateway.core.http.HttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public class RouteUtil {

    public static Supplier<CompletionStage<Response>> buildRouteSupplier(GatewayContext context) {
        return () -> {
            Request request = context.getRequest().build();
            CompletableFuture<Response> future = HttpClient.getInstance().executeRequest(request);
            future.whenComplete(((response, throwable) -> {
                if (throwable != null) {
                    context.setThrowable(throwable);
                    throw new RuntimeException(throwable);
                }
                context.setResponse(ResponseHelper.buildGatewayResponse(response));
                context.doFilter();
            }));
            return future;
        };
    }

}
