package com.authenticator.account.architecture.presentation;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.authenticator.account.authentication.Constants;
import com.authenticator.account.authentication.Credentials;
import com.authenticator.account.broadcast.BroadcastManager;
import com.authenticator.account.broadcast.auth.AccountCreationBroadcastMessage;
import com.authenticator.account.architecture.view.SignUpView;
import com.authenticator.account.service.AuthService;
import com.authenticator.account.ui.ActivityScope;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

@ActivityScope
public class DefaultSignUpPresenter implements SignUpPresenter {

    private final BroadcastManager broadcastManager;

    private final SignUpView view;

    private final Bundle viewExtras;

    @Inject
    DefaultSignUpPresenter(@NonNull SignUpView view,
                           @NonNull Bundle viewExtras,
                           @NonNull BroadcastManager broadcastManager) {
        Preconditions.checkNotNull(view);
        Preconditions.checkNotNull(viewExtras);
        Preconditions.checkNotNull(broadcastManager);
        this.view = view;
        this.viewExtras = viewExtras;
        this.broadcastManager = broadcastManager;
    }

    @Override
    public void createAccount() {
        final String username = view.getUsername();
        final String email = view.getEmail();
        final String password = view.getPassword();

        final Credentials credentials = Credentials.builder()
                .setUsername(username)
                .setEmail(email)
                .setPassword(password)
                .build();

        broadcastManager.register(this);
        AuthService.createAccount((Context) view, credentials);
    }

    @Override
    public void destroy() {
        broadcastManager.unregister(this);
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(AccountCreationBroadcastMessage message) {
        if (view.isFinishing()) {
            return;
        }

        final Bundle data = message.getData();

        if (data.containsKey(AccountManager.KEY_ERROR_MESSAGE)) {
            final String errorMessage = data.getString(AccountManager.KEY_ERROR_MESSAGE);
            view.showToast(errorMessage);
        } else {
            final Intent intent = new Intent().putExtras(data);

            view.setResult(Activity.RESULT_OK, intent);
            view.finish();
        }
    }
}
