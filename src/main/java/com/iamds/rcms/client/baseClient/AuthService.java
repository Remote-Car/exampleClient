package com.iamds.rcms.client.baseClient;

import java.io.IOException;
import java.util.HashMap;

public abstract class AuthService {

    private final static AuthServiceAbstract AUTH_SERVICE = new AuthServiceDefault();

    public static AuthToken getAuthToken(ApiEndpoint apiEndpoint, String email, String password) throws RcmsClientException, IOException {
        return AUTH_SERVICE.getAuthToken(apiEndpoint, email, password);
    }

    public static void refreshAuthToken(ApiEndpoint apiEndpoint, AuthToken authToken) throws RcmsClientException, IOException {
        AUTH_SERVICE.refreshAuthToken(apiEndpoint, authToken);
    }

    public static boolean isTokenStillValid(AuthToken authToken) {
        return AuthServiceAbstract.isTokenStillValid(authToken);
    }

    public static ActionResponse runAction(ApiEndpoint apiEndpoint, AuthToken authToken, String command, ActionTypes actionType, HashMap<String, String> params, String body) throws IOException, RcmsClientException {
        return AUTH_SERVICE.runAction(apiEndpoint, authToken, command, actionType, params, body);
    }
}
