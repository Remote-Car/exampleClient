package com.iamds.rcms.client.baseClient;

public class ApiEndpoint {
    private String url;
    private String clientKey;

    public ApiEndpoint(String url, String clientKey) {
        this.url = url;
        this.clientKey = clientKey;
    }

    public String getUrl() {
        return url;
    }

    public String getClientKey() {
        return clientKey;
    }
}
