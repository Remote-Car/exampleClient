package com.iamds.rcms.client.baseClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class AuthService {

    public static AuthToken getAuthToken(ApiEndpoint apiEndpoint, String email, String password) throws RcmsClientException, IOException {
        HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpPost post = new HttpPost(apiEndpoint.getUrl()+"v1/auth/login?email="+URLEncoder.encode(email, "UTF-8")+"&password="+URLEncoder.encode(password, "UTF-8")+"&authType=USER&clientKey="+URLEncoder.encode(apiEndpoint.getClientKey(), "UTF-8"));

        HttpResponse response = client.execute(post);
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

        AuthToken authToken = new AuthToken((String) authTokenData.get("accessToken"), (String) authTokenData.get("refreshToken"), (String) authTokenData.get("lang"), (String) authTokenData.get("authType"), (Long) authTokenData.get("expiresIn"), (Long) authTokenData.get("accountId"));
        return authToken;
    }

    public static void refreshAuthToken(ApiEndpoint apiEndpoint, AuthToken authToken) throws RcmsClientException, IOException {
        if(!isTokenStillValid(authToken)) {
            throw new RcmsClientException("Could not refresh authToken. Expired!");
        }
        HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpPost post = new HttpPost(apiEndpoint.getUrl()+"v1/auth/refresh?refreshToken="+URLEncoder.encode(authToken.getRefreshToken(), "UTF-8"));

        post.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

        HttpResponse response = client.execute(post);
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
        JSONObject jsonObject = (JSONObject) obj;
        if(jsonObject.get("accessToken") == null || jsonObject.get("refreshToken") == null || jsonObject.get("lang") == null || jsonObject.get("authType") == null || jsonObject.get("expiresIn") == null || jsonObject.get("accountId") == null)
            throw new RcmsClientException("getAuthToken returned JSON with missing parameters! response: "+response.getStatusLine().getStatusCode()+", "+content);

        authToken.updateAuthToken((String) jsonObject.get("accessToken"), (String) jsonObject.get("refreshToken"), (String) jsonObject.get("lang"), (String) jsonObject.get("authType"), (Long) jsonObject.get("expiresIn"), (Long) jsonObject.get("accountId"));
    }

    public static boolean isTokenStillValid(AuthToken authToken) {
        if(authToken.getExpiresAt()-5000 > System.currentTimeMillis())
            return true;
        return false;
    }

    public static ActionResponse runAction(ApiEndpoint apiEndpoint, AuthToken authToken, String command, ActionTypes actionType, HashMap<String, String> params, String body) throws ClientProtocolException, IOException, RcmsClientException {
    	HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        String url = apiEndpoint.getUrl()+command;
        String paramStr = "";
        if(params != null)
	        for(String paramKey : params.keySet()) {
	        	String paramVal = params.get(paramKey);
	        	if(paramStr.length() == 0)
	        		paramStr += "?";
	        	else
	        		paramStr += "&";
	        	paramStr += URLEncoder.encode(paramKey, "UTF-8") + "=" + URLEncoder.encode(paramVal, "UTF-8");
	        }
        url += paramStr;
        HttpResponse response = null;

    	if(actionType == ActionTypes.POST) {
            HttpPost post = new HttpPost(url);
            if(body != null)
            	post.setEntity(new ByteArrayEntity(
            	    body.toString().getBytes("UTF8")));

            post.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

            response = client.execute(post);
        }
    	else if(actionType == ActionTypes.PATCH) {
            HttpPatch patch = new HttpPatch(url);
            if(body != null)
            	patch.setEntity(new ByteArrayEntity(
            	    body.toString().getBytes("UTF8")));

            patch.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

            response = client.execute(patch);
        }
    	else if(actionType == ActionTypes.GET) {
            HttpGet get = new HttpGet(url);

            get.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

            response = client.execute(get);
        }
    	else if(actionType == ActionTypes.DELETE) {
            HttpDelete delete = new HttpDelete(url);

            delete.addHeader("Authorization", "Bearer "+authToken.getAccessToken());

            response = client.execute(delete);
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
        
        ActionResponse actionResponse = new ActionResponse(response.getStatusLine().getStatusCode(), jsonObject, jsonArray);
        
    	return actionResponse;
    }
}
