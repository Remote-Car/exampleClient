package com.iamds.rcms.client.baseClient;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.client.WinHttpClients;

public class AuthServiceWinWebProxy extends AuthServiceAbstract {

    private final String proxyAddress;
    private final int proxyPort;

    public AuthServiceWinWebProxy(String proxyAddress, int proxyPort) {
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
    }

    @Override
    public CloseableHttpClient client() throws RcmsClientException {

        if(!WinHttpClients.isWinAuthAvailable()) {
            throw new RcmsClientException("Integrated Win auth is not supported");
        }

        return WinHttpClients
                .custom()
                .setProxy(new HttpHost(proxyAddress, proxyPort))
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }
}
