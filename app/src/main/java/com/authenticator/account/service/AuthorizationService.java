package com.authenticator.account.service;

import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.authenticator.account.auth.Auth;
import com.authenticator.account.auth.ServerAuthCallbacksFactory;
import com.authenticator.account.exception.AuthException;
import com.authenticator.account.interfaces.ServerAuthenticateCallbacks;
import com.authenticator.account.receiver.AuthorizationReceiver;

public class AuthorizationService extends IntentService {

    private ServerAuthenticateCallbacks serverAuthenticate;

    public AuthorizationService() {
        super(AuthorizationService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serverAuthenticate = ServerAuthCallbacksFactory.construct(ServerAuthenticateCallbacks.Server.PARSE_COM);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final String username = intent.getStringExtra(Auth.USER_NAME);
        final String password = intent.getStringExtra(Auth.PASSWORD);
        final String accountType = intent.getStringExtra(Auth.ACCOUNT_TYPE);
        final String authTokenType = intent.getStringExtra(Auth.AUTH_TOKEN_TYPE);

        final Bundle data = new Bundle();

        try {
            String authToken = serverAuthenticate.onUserSignIn(username, password, authTokenType);
            data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
            data.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            data.putString(AccountManager.KEY_PASSWORD, password);

        } catch (AuthException e) {
            data.putString(AccountManager.KEY_ERROR_MESSAGE, e.getMessage());
        }

        final Intent resultIntent = new Intent(AuthorizationReceiver.BROADCAST);
        resultIntent.putExtras(data);
        sendOrderedBroadcast(resultIntent, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serverAuthenticate = null;
    }
}
