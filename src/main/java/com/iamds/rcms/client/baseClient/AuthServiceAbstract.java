package com.iamds.rcms.client.baseClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class AuthServiceAbstract {

    public abstract CloseableHttpClient client() throws RcmsClientException;

    public AuthToken getAuthToken(ApiEndpoint apiEndpoint, String email, String password) throws RcmsClientException, IOException {
        HttpPost post = new HttpPost(apiEndpoint.getUrl()+"v1/auth/login?email="+ URLEncoder.encode(email, UTF_8.name())+"&password="+URLEncoder.encode(password, UTF_8.name())+"&authType=USER&clientKey="+URLEncoder.encode(apiEndpoint.getClientKey(), UTF_8.name()));

        HttpResponse response = client().execute(post);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        if(response.getStatusLine().getStatusCode() != 200)
            throw new RcmsClientException("getAuthToken returned unexpected response! Expected 200, got: "+response.getStatusLine().getStatusCode()+", "+content);

        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RcmsClientException("Failed to parse response JSON for 'getAuthToken', "+content);
        }
        JSONObject jsonObject = (JSONObject) obj;
        if(jsonObject.get("authTokenData") == null)
            throw new RcmsClientException("getAuthToken returned JSON with missing parameters! response: "+response.getStatusLine().getStatusCode()+", "+content);
        JSONObject authTokenData = (JSONObject) jsonObject.get("authTokenData");
        if(authTokenData.get("accessToken") == null || authTokenData.get("refreshToken") == null || authTokenData.get("lang") == null || authTokenData.get("authType") == null || authTokenData.get("expiresIn") == null || authTokenData.get("accountId") == null)
            throw new RcmsClientException("getAuthToken returned JSON with missing parameters! response: "+response.getStatusLine().getStatusCode()+", "+content);

        return new AuthToken((String) authTokenData.get("accessToken"), (String) authTokenData.get("refreshToken"), (String) authTokenData.get("lang"), (String) authTokenData.get("authType"), (Long) authTokenData.get("expiresIn"), (Long) authTokenData.get("accountId"));
    }

    public void refreshAuthToken(ApiEndpoint apiEndpoint, AuthToken authToken) throws RcmsClientException, IOException {
        if(!isTokenStillValid(authToken)) {
            throw new RcmsClientException("Could not refresh authToken. Expired!");
        }
        HttpPost post = new HttpPost(apiEndpoint.getUrl()+"v1/auth/refresh?refreshToken="+URLEncoder.encode(authToken.getRefreshToken(), UTF_8.name()));

        post.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

        HttpResponse response = client().execute(post);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        if(response.getStatusLine().getStatusCode() != 200)
            throw new RcmsClientException("refreshAuthToken returned unexpected response! Expected 200, got: "+response.getStatusLine().getStatusCode()+", "+content);

        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RcmsClientException("Failed to parse response JSON for 'refreshAuthToken', "+content);
        }
        JSONObject jsonObject = (JSONObject)((JSONObject) obj).get("authTokenData");
        if(jsonObject.get("accessToken") == null || jsonObject.get("refreshToken") == null || jsonObject.get("lang") == null || jsonObject.get("authType") == null || jsonObject.get("expiresIn") == null || jsonObject.get("accountId") == null)
            throw new RcmsClientException("getAuthToken returned JSON with missing parameters! response: "+response.getStatusLine().getStatusCode()+", "+content);

        authToken.updateAuthToken((String) jsonObject.get("accessToken"), (String) jsonObject.get("refreshToken"), (String) jsonObject.get("lang"), (String) jsonObject.get("authType"), (Long) jsonObject.get("expiresIn"), (Long) jsonObject.get("accountId"));
    }

    public static boolean isTokenStillValid(AuthToken authToken) {
        return authToken.getExpiresAt() - 5000 > System.currentTimeMillis();
    }

    public ActionResponse runAction(ApiEndpoint apiEndpoint, AuthToken authToken, String command, ActionTypes actionType, HashMap<String, String> params, String body) throws IOException, RcmsClientException {

        StringBuilder paramStr = new StringBuilder();
        if(params != null)
            for(String paramKey : params.keySet()) {
                String paramVal = params.get(paramKey);
                if(paramStr.length() == 0)
                    paramStr.append("?");
                else
                    paramStr.append("&");
                paramStr.append(URLEncoder.encode(paramKey, UTF_8.name())).append("=").append(URLEncoder.encode(paramVal, UTF_8.name()));
            }

        String url = apiEndpoint.getUrl()+command+paramStr;
        HttpResponse response;

        if(actionType == ActionTypes.POST) {
            HttpPost post = new HttpPost(url);
            if(body != null)
                post.setEntity(new ByteArrayEntity(
                        body.getBytes(UTF_8)));

            post.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

            response = client().execute(post);
        }
        else if(actionType == ActionTypes.PATCH) {
            HttpPatch patch = new HttpPatch(url);
            if(body != null)
                patch.setEntity(new ByteArrayEntity(
                        body.getBytes(UTF_8)));

            patch.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

            response = client().execute(patch);
        }
        else if(actionType == ActionTypes.GET) {
            HttpGet get = new HttpGet(url);

            get.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

            response = client().execute(get);
        }
        else if(actionType == ActionTypes.DELETE) {
            HttpDelete delete = new HttpDelete(url);

            delete.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

            response = client().execute(delete);
        } else {
            throw new RcmsClientException("unsupported action type");
        }

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RcmsClientException("Failed to parse response JSON for 'runAction', "+content);
        }
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        if(obj.getClass() == JSONObject.class)
            jsonObject = (JSONObject) obj;
        else if(obj.getClass() == JSONArray.class)
            jsonArray = (JSONArray) obj;

        return new ActionResponse(response.getStatusLine().getStatusCode(), jsonObject, jsonArray);
    }
}
