package com.grace.gateway.core.http;


import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.concurrent.CompletableFuture;

public class HttpClient {

    private AsyncHttpClient asyncHttpClient;

    private HttpClient() {
    }

    private final static HttpClient INSTANCE = new HttpClient();

    public static HttpClient getInstance() {
        return INSTANCE;
    }

    public void initialized(AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    public CompletableFuture<Response> executeRequest(Request request) {
        ListenableFuture<Response> future = asyncHttpClient.executeRequest(request);
        return future.toCompletableFuture();
    }


}
