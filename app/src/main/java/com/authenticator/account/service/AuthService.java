package com.authenticator.account.service;

import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.authenticator.account.authentication.Credentials;
import com.authenticator.account.broadcast.BroadcastManager;
import com.authenticator.account.broadcast.auth.AccountCreationBroadcastMessage;
import com.authenticator.account.broadcast.auth.SignInBroadcastMessage;
import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.di.Qualifiers;
import com.authenticator.account.exception.AuthenticationException;
import com.authenticator.account.authentication.AuthenticationProvider;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.inject.Named;

public class AuthService extends IntentService {

    private static final String TAG = AuthService.class.getSimpleName();

    private static final String EXTRA_REQUEST = "request";

    private static final String EXTRA_CREDENTIALS = "credentials";

    @IntDef({
            AuthRequest.REQUEST_CREATE_ACCOUNT,
            AuthRequest.REQUEST_SIGN_IN
    })
    @interface AuthRequest {

        int REQUEST_CREATE_ACCOUNT = 1;

        int REQUEST_SIGN_IN = 2;
    }

    public static void createAccount(@NonNull Context context, @NonNull Credentials credentials) {
        shipWithRequest(context, credentials, AuthRequest.REQUEST_CREATE_ACCOUNT);
    }

    public static void signIn(@NonNull Context context, @NonNull Credentials credentials) {
        shipWithRequest(context, credentials, AuthRequest.REQUEST_SIGN_IN);
    }

    private static void shipWithRequest(@NonNull final Context context,
                                        @NonNull final Credentials credentials,
                                        @AuthRequest final int request) {
        final Intent intent = new Intent(context, AuthService.class)
                .putExtra(EXTRA_REQUEST, request)
                .putExtra(EXTRA_CREDENTIALS, credentials);

        context.startService(intent);
    }

    @Inject
    @Named(Qualifiers.PARSE_AUTHENTICATION_PROVIDER)
    AuthenticationProvider authenticationProvider;

    @Inject
    BroadcastManager broadcastManager;

    public AuthService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DependencyInjector.getGraph().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final int request = intent.getIntExtra(EXTRA_REQUEST, Integer.MAX_VALUE);
        final Credentials credentials = intent.getParcelableExtra(EXTRA_CREDENTIALS);

        switch (request) {

            case AuthRequest.REQUEST_CREATE_ACCOUNT:
                onCreateAccount(credentials);
                break;

            case AuthRequest.REQUEST_SIGN_IN:
                onSignIn(credentials);
                break;

            default:
                throw new UnsupportedOperationException("Unknown request id to be handled: " + request);
        }
    }

    private void onCreateAccount(Credentials credentials) {
        final Bundle data = new Bundle();

        try {
            String authToken = authenticationProvider.signUp(credentials);
            data.putString(AccountManager.KEY_ACCOUNT_NAME, credentials.getUsername());
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, credentials.getAccountType());
            data.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            data.putString(AccountManager.KEY_PASSWORD, credentials.getPassword());
        } catch (AuthenticationException e) {
            data.putString(AccountManager.KEY_ERROR_MESSAGE, e.getMessage());
        }

        final AccountCreationBroadcastMessage message = new AccountCreationBroadcastMessage(data);

        sendData(message);
    }

    private void onSignIn(Credentials credentials) {
        final Bundle data = new Bundle();

        try {
            String authToken = authenticationProvider.signIn(credentials);
            data.putString(AccountManager.KEY_ACCOUNT_NAME, credentials.getUsername());
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, credentials.getAccountType());
            data.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            data.putString(AccountManager.KEY_PASSWORD, credentials.getPassword());
        } catch (AuthenticationException e) {
            data.putString(AccountManager.KEY_ERROR_MESSAGE, e.getMessage());
        }

        final SignInBroadcastMessage message = new SignInBroadcastMessage(data);

        sendData(message);
    }

    void sendData(@NonNull Object object) {
        Preconditions.checkNotNull(object, "Post Object cannot be null.");
        broadcastManager.post(object);
    }
}
