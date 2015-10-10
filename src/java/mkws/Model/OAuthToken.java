/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.Model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author onurerden
 */
public class OAuthToken {

    private String access_token = "Not Set";
    private String expires_in = "Not Set";
    private String refresh_token = "Not Set";
    private String scope = "Not Set";

    private String clientId = "Not Set";
    private String clientSecret = "Not Set";

    private String authorizationCode = "Not Set";

    public String refreshToken() {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost("https://oauth2-api.mapmyapi.com/v7.1/oauth2/uacf/access_token/");
            httppost.addHeader("Api-Key", getClientId());

            MultipartEntity mpEntity = new MultipartEntity();
            mpEntity.addPart("grant_type", new StringBody("refresh_token"));
            mpEntity.addPart("refresh_token", new StringBody(getRefresh_token()));
            mpEntity.addPart("client_id", new StringBody(getClientId()));
            mpEntity.addPart("client_secret", new StringBody(getClientSecret()));

            httppost.setEntity(mpEntity);
            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            String responseString = EntityUtils.toString(resEntity, "UTF-8");
            if (responseString.contains("error")) {
                System.out.println("Error while refresh token: " + responseString);
                return "Error while refresh token: " + responseString;
            }
            System.out.println(responseString);

            Gson jsonObject = new Gson();
            OAuthToken tokenObject;
            tokenObject = jsonObject.fromJson(responseString, OAuthToken.class);

            setAccess_token(tokenObject.access_token);
            setExpires_in(tokenObject.expires_in);
            setRefresh_token(tokenObject.refresh_token);
            setScope(tokenObject.scope);

            printToken();

        } catch (IOException | ParseException | JsonSyntaxException ex) {
            return "Error while refresh token";
        }
        return getAccess_token();
    }

    public String getNewAccessToken() {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost("https://oauth2-api.mapmyapi.com/v7.1/oauth2/uacf/access_token/");
            httppost.addHeader("Api-Key", getClientId());

            MultipartEntity mpEntity = new MultipartEntity();
            mpEntity.addPart("grant_type", new StringBody("authorization_code"));
            mpEntity.addPart("code", new StringBody(getAuthorizationCode()));
            mpEntity.addPart("client_id", new StringBody(getClientId()));
            mpEntity.addPart("client_secret", new StringBody(getClientSecret()));

            httppost.setEntity(mpEntity);
            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            String responseString = EntityUtils.toString(resEntity, "UTF-8");
            if (responseString.contains("error")) {
                System.out.println("Error while access token: " + responseString);
                return "Error while access token: " + responseString;
            }
            System.out.println(responseString);

            Gson jsonObject = new Gson();
            OAuthToken tokenObject;
            tokenObject = jsonObject.fromJson(responseString, OAuthToken.class);

            setAccess_token(tokenObject.access_token);
            setExpires_in(tokenObject.expires_in);
            setRefresh_token(tokenObject.refresh_token);
            setScope(tokenObject.scope);

            printToken();

        } catch (IOException | ParseException | JsonSyntaxException ex) {
            System.out.println("Error while accessToken: " + ex.getMessage());
        }

        return getAccess_token();
    }

    public void printToken() {
        System.out.println("Granted Token:");
        System.out.println("    Access Token : " + getAccess_token());
        System.out.println("    Expires In   : " + getExpires_in());
        System.out.println("    Refresh Token: " + getRefresh_token());
        System.out.println("    Scope        : " + getScope());
    }

    /**
     * @return the access_token
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * @param access_token the access_token to set
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /**
     * @return the expires_in
     */
    public String getExpires_in() {
        return expires_in;
    }

    /**
     * @param expires_in the expires_in to set
     */
    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    /**
     * @return the refresh_token
     */
    public String getRefresh_token() {
        return refresh_token;
    }

    /**
     * @param refresh_token the refresh_token to set
     */
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    /**
     * @return the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * @param scope the scope to set
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the clientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * @param clientSecret the clientSecret to set
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * @return the authorizationCode
     */
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * @param authorizationCode the authorizationCode to set
     */
    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

}
