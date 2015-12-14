package com.authenticator.account.authentication;

import android.support.annotation.NonNull;

import com.authenticator.account.BuildConfig;
import com.authenticator.account.exception.AuthenticationException;
import com.authenticator.account.architecture.model.ParseResponse;
import com.google.common.base.Preconditions;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import retrofit.Call;
import retrofit.MoshiConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ParseAuthenticationProvider implements AuthenticationProvider {

    private static final String BASE_URL = "https://api.parse.com";

    private static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";

    static {
        JSONObject credentialsCreator = new JSONObject();
        try {
            credentialsCreator.put(Constants.Parse.PARAMETER_USERNAME, "%s");
            credentialsCreator.put(Constants.Parse.PARAMETER_PASSWORD, "%s");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create Http Post request!");
        }
    }

    private final ParseAuthenticationService authenticationService;

    public ParseAuthenticationProvider(final @NonNull OkHttpClient client) {

        authenticationService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create(ParseAuthenticationService.class);
    }


    @Override
    public String signUp(@NonNull Credentials credentials) throws AuthenticationException {
        final String name = credentials.getUsername();
        final String password = credentials.getPassword();

        final RequestBody requestBody = createSignUpRequestBody(name, password);

        final Call<ParseResponse> call = authenticationService.signUp(BuildConfig.PARSE_APPLICATION_ID,
                BuildConfig.PARSE_API_KEY,
                MEDIA_TYPE_APPLICATION_JSON,
                requestBody);

        try {
            final Response<ParseResponse> response = call.execute();

            Preconditions.checkState(response.code() == HttpStatus.SC_CREATED);

            final ParseResponse parseResponse = response.body();

            return parseResponse.sessionToken;
        } catch (Exception e) {
            throw new AuthenticationException("");
        }
    }

    @Override
    public String signIn(@NonNull Credentials credentials) throws AuthenticationException {

        final String username = credentials.getUsername();
        final String password = credentials.getPassword();

        try {
            final Call<ParseResponse> call = authenticationService.signIn(BuildConfig.PARSE_APPLICATION_ID,
                    BuildConfig.PARSE_API_KEY,
                    URLEncoder.encode(username, "UTF-8"),
                    password);

            final Response<ParseResponse> response = call.execute();


            Preconditions.checkState(response.code() == HttpStatus.SC_OK);

            final ParseResponse parseResponse = response.body();

            return parseResponse.sessionToken;
        } catch (Exception e) {
            throw new AuthenticationException("");
        }
    }

    private static RequestBody createSignUpRequestBody(@NonNull final String username,
                                                       @NonNull final String password) {
        final JSONObject credentialsCreator = new JSONObject();
        try {
            credentialsCreator.put(Constants.Parse.PARAMETER_USERNAME, username);
            credentialsCreator.put(Constants.Parse.PARAMETER_PASSWORD, password);

            final MediaType mediaType = MediaType.parse(MEDIA_TYPE_APPLICATION_JSON);
            final String content = credentialsCreator.toString();

            return RequestBody.create(mediaType, content);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create Http Post request!");
        }
    }
}
