package com.authenticator.account.architecture.presentation;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.authenticator.account.architecture.view.SignInView;
import com.authenticator.account.authentication.Credentials;
import com.authenticator.account.broadcast.BroadcastManager;
import com.authenticator.account.broadcast.auth.SignInBroadcastMessage;
import com.authenticator.account.service.AuthService;
import com.authenticator.account.ui.ActivityScope;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

@ActivityScope
public class DefaultSignInPresenter implements SignInActivityPresenter {

    private final SignInView view;
    private final AccountManager accountManager;
    private final BroadcastManager broadcastManager;

    @Inject
    public DefaultSignInPresenter(@NonNull SignInView view,
                                  @NonNull AccountManager accountManager,
                                  @NonNull BroadcastManager broadcastManager) {
        Preconditions.checkNotNull(view);
        Preconditions.checkNotNull(accountManager);
        Preconditions.checkNotNull(broadcastManager);
        this.view = view;
        this.accountManager = accountManager;
        this.broadcastManager = broadcastManager;
    }

    @Override
    public void requestAccess() {
        final String username = view.getUsername();
        final String password = view.getPassword();
        final String authTokenType = view.getAuthTokenType();

        final Credentials credentials = Credentials.builder()
                .setUsername(username)
                .setPassword(password)
                .setAuthTokenType(authTokenType)
                .build();

        broadcastManager.register(this);
        AuthService.signIn((Context) view, credentials);
    }

    @Override
    public void notifyUserSignedUp(@NonNull Bundle data) {
        addOrUpdateAccount(data);
    }

    @Override
    public void destroy() {
        broadcastManager.unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(SignInBroadcastMessage signInBroadcastMessage) {
        final Bundle result = signInBroadcastMessage.getData();
        if (result.containsKey(AccountManager.KEY_ERROR_MESSAGE)) {
            view.onErrorOccured(result.getString(AccountManager.KEY_ERROR_MESSAGE));
        } else {
            addOrUpdateAccount(result);
        }
    }

    private void addOrUpdateAccount(@NonNull Bundle result) {
        addAccountOrUpdateWithPassword(result);

        Intent resultIntent = new Intent().putExtras(result);
        view.setResult(Activity.RESULT_OK, resultIntent);
        view.onUserSignedIn(result);
    }

    private void addAccountOrUpdateWithPassword(final Bundle bundle) {
        final String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
        final String accountPassword = bundle.getString(AccountManager.KEY_PASSWORD);
        final String accountType = bundle.getString(AccountManager.KEY_ACCOUNT_TYPE);
        final String authTokenType = view.getAuthTokenType();
        final boolean isAddingNewAccount = view.isNewAccountRequested();

        final Account account = new Account(accountName, accountType);

        if (isAddingNewAccount) {
            String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

            accountManager.addAccountExplicitly(account, accountPassword, null);
            accountManager.setAuthToken(account, authTokenType, authToken);
        } else {
            accountManager.setPassword(account, accountPassword);
        }
    }
}
