package com.iamds.rcms.client.baseClient;

public class AuthToken {

    // authTokenData (Data retrieved by API)
    private String accessToken;
    private String refreshToken;
    private String lang;
    private String authType;
    private Long expiresIn;
    private Long accountId;

    // Expire time calculated with System.currentTimeMillis
    private Long expiresAt;

    public AuthToken(String accessToken, String refreshToken, String lang, String authType, Long expiresIn, Long accountId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.lang = lang;
        this.authType = authType;
        this.expiresIn = expiresIn;
        this.accountId = accountId;

        this.expiresAt = System.currentTimeMillis() + (expiresIn);
    }

    public void updateAuthToken(String accessToken, String refreshToken, String lang, String authType, Long expiresIn, Long accountId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.lang = lang;
        this.authType = authType;
        this.expiresIn = expiresIn;
        this.accountId = accountId;

        this.expiresAt = System.currentTimeMillis() + (expiresIn);
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
