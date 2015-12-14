package com.authenticator.account.authentication;

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
import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.di.Qualifiers;
import com.authenticator.account.exception.AuthenticationException;
import com.authenticator.account.ui.SignInActivity;
import com.google.common.base.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import autovalue.shaded.com.google.common.common.base.Joiner;

public class SimpleAuthenticator extends AbstractAccountAuthenticator {

    @Inject
    AccountManager accountManager;

    @Inject
    @Named(Qualifiers.PARSE_AUTHENTICATION_PROVIDER)
    AuthenticationProvider parseAuthenticationProvider;

    private final Context context;

    public SimpleAuthenticator(Context context) {
        super(context);
        DependencyInjector.getGraph().inject(this);
        this.context = context;
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

        final Intent intent = new Intent(context, SignInActivity.class)
                .putExtra(Constants.ACCOUNT_TYPE, accountType)
                .putExtra(Constants.AUTH_TOKEN_TYPE, authTokenType)
                .putExtra(Constants.IS_NEW_ACCOUNT, true)
                .putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

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

        if (!Objects.equal(Constants.Access.AUTH_TOKEN_TYPE_READ_ONLY, authTokenType) &&
                !Objects.equal(Constants.Access.AUTH_TOKEN_FULL_ACCESS, authTokenType)) {

            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, context.getString(R.string.invalid_auth_token));
            return result;
        }

        String authenticationToken = accountManager.peekAuthToken(account, authTokenType);

        if (TextUtils.isEmpty(authenticationToken)) {
            final String password = accountManager.getPassword(account);
            if (!TextUtils.isEmpty(password)) {
                try {
                    authenticationToken = parseAuthenticationProvider.signIn(Credentials.builder()
                            .setUsername(account.name)
                            .setPassword(password)
                            .setAuthTokenType(authTokenType)
                            .build());
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!TextUtils.isEmpty(authenticationToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authenticationToken);
            return result;
        }

        final Intent intent = new Intent(context, SignInActivity.class)
                .putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
                .putExtra(Constants.ACCOUNT_TYPE, account.type)
                .putExtra(Constants.AUTH_TOKEN_TYPE, authTokenType)
                .putExtra(Constants.ACCOUNT_NAME, account.name);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (Constants.Access.AUTH_TOKEN_FULL_ACCESS.equals(authTokenType)) {
            return Constants.Access.AUTH_TOKEN_FULL_ACCESS_LABEL;
        } else if (Constants.Access.AUTH_TOKEN_TYPE_READ_ONLY.equals(authTokenType)) {
            return Constants.Access.AUTH_TOKEN_TYPE_READ_ONLY_LABEL;
        } else {
            return Joiner.on("").join(authTokenType, "(Label)");
        }

    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response,
                                    Account account,
                                    String authTokenType,
                                    Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }
}
