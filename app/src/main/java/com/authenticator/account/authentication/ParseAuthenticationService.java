package com.authenticator.account.authentication;

import com.authenticator.account.architecture.model.ParseResponse;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

public interface ParseAuthenticationService {

    @POST("/1/users")
    Call<ParseResponse> signUp(@Header("X-Parse-Application-Id") String parseApplicationId,
                          @Header("X-Parse-REST-API-Key") String parseApiKey,
                          @Header("Content-Type") String contentType,
                          @Body RequestBody requestBody);

    @GET("/1/login")
    Call<ParseResponse> signIn(@Header("X-Parse-Application-Id") String parseApplicationId,
                         @Header("X-Parse-REST-API-Key") String parseApiKey,
                         @Query("username") String username,
                         @Query("password") String password);

}
