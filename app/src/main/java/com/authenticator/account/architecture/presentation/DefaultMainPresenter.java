package com.authenticator.account.architecture.presentation;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.authenticator.account.R;
import com.authenticator.account.architecture.view.MainView;
import com.authenticator.account.authentication.Constants;
import com.authenticator.account.controller.BackgroundExecutor;
import com.authenticator.account.ui.ActivityScope;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

@ActivityScope
public class DefaultMainPresenter implements MainPresenter {

    private final MainView view;

    private final Activity activity;

    private final AccountManager accountManager;

    private final BackgroundExecutor executor;

    @Inject
    public DefaultMainPresenter(@NonNull MainView view,
                                @NonNull Activity activity,
                                @NonNull AccountManager accountManager,
                                @NonNull BackgroundExecutor executor) {
        Preconditions.checkNotNull(view);
        Preconditions.checkNotNull(accountManager);
        Preconditions.checkNotNull(executor);
        this.view = view;
        this.activity = activity;
        this.accountManager = accountManager;
        this.executor = executor;
    }

    @Override
    public void addNewAccount(@NonNull String access) {
        Preconditions.checkNotNull(access);
        accountManager.addAccount(Constants.ACCOUNT_TYPE,
                access,
                null,
                null,
                activity,
                future1 -> {
                    try {
                        future1.getResult();
                        view.onAccountAdded();
                    } catch (Exception e) {
                        view.onErrorOccured(e.getMessage());
                    }
                },
                null);
    }

    @Override
    public void getAuthenticationTokenByFeatures(@NonNull String access) {
        Preconditions.checkNotNull(access);
        accountManager.getAuthTokenByFeatures(Constants.ACCOUNT_TYPE,
                access,
                null,
                activity,
                null,
                null,
                future1 -> {
                    try {
                        Bundle data = future1.getResult();
                        final String authToken = data.getString(AccountManager.KEY_AUTHTOKEN);
                        view.onRequestedTokenObtained(authToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.onErrorOccured(e.getMessage());
                    }
                }, null);
    }

    @Override
    public List<String> getApplicationAccountNames() {
        Account[] availableAccounts = getAccountsByType();

        final int size = availableAccounts.length;

        if (size == 0) {
            return Collections.emptyList();
        }

        final ImmutableList.Builder<String> builder = ImmutableList.builder();

        for (final Account account : availableAccounts) {
            builder.add(account.name);
        }

        return builder.build();
    }

    @Override
    public Account getRegisteredAccount(int index) {
        return getAccountsByType()[index];
    }

    @Override
    public void removeAccount(int which) {
        final Account account = getAccountsByType()[which];
        accountManager.removeAccount(account, future -> {
                    try {
                        future.getResult();
                        view.onAccountRemoved(account);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.onErrorOccured(e.getMessage());
                    }
                },
                null);
    }

    @Override
    public void loadExistingAccountAuthToken(int which, @NonNull String access) {
        final Account account = getRegisteredAccount(which);
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                access,
                null,
                activity,
                null,
                null);
        executor.execute(() -> {
            try {
                Bundle bnd = future.getResult();

                final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                if (!TextUtils.isEmpty(authtoken)) {
                    view.onAuthTokenLoaded(authtoken);
                } else {
                    view.onErrorOccured(activity.getString(R.string.error));
                }
            } catch (Exception e) {
                e.printStackTrace();
                view.onErrorOccured(e.getMessage());
            }
        });
    }

    @Override
    public void invalidateAuthToken(int which, @NonNull String access) {
        final Account account = getRegisteredAccount(which);
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                access,
                null,
                activity,
                null,
                null);
        executor.execute(() -> {
            try {
                Bundle result = future.getResult();
                final String authtoken = result.getString(AccountManager.KEY_AUTHTOKEN);
                accountManager.invalidateAuthToken(account.type, authtoken);
                view.onAuthTokenInvalidated(account);
            } catch (Exception e) {
                e.printStackTrace();
                view.onErrorOccured(e.getMessage());
            }
        });
    }

    @Override
    public void showAccountsPicker(@NonNull DialogInterface.OnClickListener clickListener) {
        Preconditions.checkNotNull(clickListener);

        final List<String> accountsNames = getApplicationAccountNames();
        if (accountsNames.isEmpty()) {
            view.onNoAccountsCreated();
        } else {
            final ArrayAdapter adapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_list_item_1, accountsNames);
            new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.pick_account))
                    .setAdapter(adapter, clickListener)
                    .create()
                    .show();
        }

    }

    @Override
    public void destroy() {
        executor.destroy();
    }

    private Account[] getAccountsByType() {
        return accountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
    }
}
