package com.iamds.rcms.client.baseClient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

public class AuthServiceDefault extends AuthServiceAbstract{

    @Override
    public CloseableHttpClient client() {
                return HttpClientBuilder
                .create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }
}
