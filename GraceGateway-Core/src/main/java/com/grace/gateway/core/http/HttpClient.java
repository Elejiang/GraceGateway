package com.grace.gateway.core.http;


import org.asynchttpclient.AsyncHttpClient;

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


}
