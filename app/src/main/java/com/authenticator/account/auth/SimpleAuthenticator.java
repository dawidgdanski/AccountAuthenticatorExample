package com.authenticator.account.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.authenticator.account.R;
import com.authenticator.account.exception.AuthException;
import com.authenticator.account.interfaces.ServerAuthenticateCallbacks;
import com.authenticator.account.ui.AuthenticatorActivity;

public class SimpleAuthenticator extends AbstractAccountAuthenticator {

    private final Context mContext;

    public SimpleAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response,
                             String accountType,
                             String authTokenType,
                             String[] requiredFeatures,
                             Bundle options) throws NetworkErrorException {

        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(Auth.ACCOUNT_TYPE, accountType);
        intent.putExtra(Auth.AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(Auth.IS_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response,
                                     Account account,
                                     Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response,
                               Account account,
                               String authTokenType,
                               Bundle options) throws NetworkErrorException {

        if(! authTokenType.equals(Auth.Access.AUTH_TOKEN_TYPE_READ_ONLY) &&
           ! authTokenType.equals(Auth.Access.AUTH_TOKEN_FULL_ACCESS)) {

           final Bundle result = new Bundle();
           result.putString(AccountManager.KEY_ERROR_MESSAGE, mContext.getString(R.string.invalid_auth_token));
           return result;
        }

        final AccountManager manager = AccountManager.get(mContext);
        String authToken =  manager.peekAuthToken(account, authTokenType);

        if(TextUtils.isEmpty(authToken)) {
            final String password = manager.getPassword(account);
            final ServerAuthenticateCallbacks serverAuthenticate = ServerAuthCallbacksFactory.construct(ServerAuthenticateCallbacks.Server.PARSE_COM);
            if(password != null) {
                try {
                    authToken = serverAuthenticate.onUserSignIn(account.name,
                            password,
                            authTokenType);
                } catch (AuthException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(Auth.ACCOUNT_TYPE, account.type);
        intent.putExtra(Auth.AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(Auth.ACCOUNT_NAME, account.name);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if(Auth.Access.AUTH_TOKEN_FULL_ACCESS.equals(authTokenType)) {
            return Auth.Access.AUTH_TOKEN_FULL_ACCESS_LABEL;
        } else if(Auth.Access.AUTH_TOKEN_TYPE_READ_ONLY.equals(authTokenType)) {
            return Auth.Access.AUTH_TOKEN_TYPE_READ_ONLY_LABEL;
        } else {
            return authTokenType + "(Label)";
        }

    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }
}
