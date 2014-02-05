package com.authenticator.account.auth;

import com.authenticator.account.exception.AuthException;
import com.authenticator.account.interfaces.ServerAuthenticateCallbacks;
import com.authenticator.account.model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ParseComServerAuthenticate implements ServerAuthenticateCallbacks {

    private final String APPLICATION_ID = "X-Parse-Application-Id";
    private final String REST_API_KEY = "X-Parse-REST-API-Key";
    private final String HTTP_POST;

    {
        JSONObject credentialsCreator = new JSONObject();
        try {
            credentialsCreator.put("username", "%s");
            credentialsCreator.put("password", "%s");

            HTTP_POST = credentialsCreator.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create Http Post request!");
        }
    }

    @Override
    public String onUserSignUp(String name, String email, String password, String authType) throws AuthException {
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(Auth.USERS_URL);
        httpPost.addHeader(APPLICATION_ID, "o7BzOSkhkBhaQb2z8f9HPPPSsUhKyqyA15q2COTh");
        httpPost.addHeader(REST_API_KEY, "YMBN4B0dNCT6rlEAPySdho87wfTMCxhBFnb0wW4I");
        httpPost.addHeader(CONTENT_TYPE, "application/json");

        try {
            HttpEntity httpEntity = new StringEntity(String.format(HTTP_POST, name, password));
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            final int statusCode = response.getStatusLine().getStatusCode();

            JSONObject userJsonObject = new JSONObject(responseString);

            if(statusCode != 201) {
                throw new AuthException(userJsonObject.getString(Auth.ERROR));
            }
            
            return userJsonObject.getString(User.JSON_SESSION_TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
            return "";

        } catch(JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String onUserSignIn(String user, String password, String authType) throws AuthException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = Auth.LOGIN_URL;

        String query = "";

        try {
            query = String.format("%s=%s&%s=%s", "username", URLEncoder.encode(user, "UTF-8"), "password", password);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url += "?" + query;

        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader(APPLICATION_ID, "o7BzOSkhkBhaQb2z8f9HPPPSsUhKyqyA15q2COTh");
        httpGet.addHeader(REST_API_KEY, "YMBN4B0dNCT6rlEAPySdho87wfTMCxhBFnb0wW4I");

        HttpParams params = new BasicHttpParams();
        params.setParameter("username", user);
        params.setParameter("password", password);
        httpGet.setParams(params);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            String responseString = EntityUtils.toString(response.getEntity());
            final int statusCode = response.getStatusLine().getStatusCode();

            JSONObject userJsonObject = new JSONObject(responseString);

            if(statusCode != 200) {
                throw new AuthException(userJsonObject.getString(Auth.ERROR));
            }

            return userJsonObject.getString(User.JSON_SESSION_TOKEN);

        } catch(IOException e) {
            e.printStackTrace();

            return "";

        } catch(JSONException e) {
            throw new IllegalStateException(e);
        }
    }
}
