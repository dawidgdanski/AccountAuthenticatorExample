package com.authenticator.account.ui;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.authenticator.account.R;
import com.authenticator.account.architecture.presentation.DefaultMainPresenter;
import com.authenticator.account.authentication.Constants;
import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.architecture.view.MainView;
import com.authenticator.account.util.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainView {

    @Bind(R.id.add_account)
    Button addAccountButton;

    @Bind(R.id.get_authtoken)
    Button getAuthtokenButton;

    @Bind(R.id.get_authtoken_by_features)
    Button getAuthtokenByFeaturesButton;

    @Bind(R.id.invalidate_auth_token)
    Button invalidateAuthTokenButton;

    @Bind(R.id.remove_account)
    Button removeAccountButton;

    @Inject
    @ActivityScope
    DefaultMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        injectMembers();
    }

    protected void bindViews() {
        ButterKnife.bind(this);
    }

    protected void injectMembers() {
        DependencyInjector.activityComponent(this).inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @OnClick(R.id.add_account)
    void onAddAccountButtonClick(View view) {
        presenter.addNewAccount(Constants.Access.AUTH_TOKEN_FULL_ACCESS);
    }

    @OnClick(R.id.get_authtoken)
    void onObtainAuthenticationTokenButtonClick(View view) {
        presenter.showAccountsPicker((dialog, which) -> presenter.loadExistingAccountAuthToken(
                which,
                Constants.Access.AUTH_TOKEN_FULL_ACCESS)
        );
    }

    @OnClick(R.id.get_authtoken_by_features)
    void onGetAuthenticationTokenByFeaturesButtonClick(View view) {
        presenter.getAuthenticationTokenByFeatures(Constants.Access.AUTH_TOKEN_FULL_ACCESS);
    }

    @OnClick(R.id.invalidate_auth_token)
    void onInvalidateAuthTokenButtonClick(View view) {
        presenter.showAccountsPicker((dialog, which) -> presenter.invalidateAuthToken(
                which,
                Constants.Access.AUTH_TOKEN_FULL_ACCESS)
        );
    }

    @OnClick(R.id.remove_account)
    void onRemoveAccountButtonClick(View view) {
        presenter.showAccountsPicker((dialog, which) -> presenter.removeAccount(which));
    }

    @Override
    public void onAccountAdded() {
        showMessage(getString(R.string.account_created_successfully));
    }

    @Override
    public void onErrorOccured(@NonNull String message) {
        showMessage(message);
    }

    @Override
    public void onRequestedTokenObtained(@Nullable String authToken) {
        showMessage(getString(R.string.authtoken, authToken));
    }

    @Override
    public void onAccountRemoved(@NonNull Account account) {
        showMessage(getString(R.string.account_removed_successfully, account.name));
    }

    @Override
    public void onAuthTokenLoaded(@NonNull String authtoken) {
        showMessage(getString(R.string.authtoken, authtoken));
    }

    @Override
    public void onAuthTokenInvalidated(@NonNull Account account) {
        showMessage(getString(R.string.account_invalidated, account.name));
    }

    @Override
    public void onNoAccountsCreated() {
        showMessage(getString(R.string.no_accounts_created));
    }

    private void showMessage(@Nullable final String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (Utils.isThisAMainThread()) {
            Utils.showToast(MainActivity.this, message);
        } else {
            runOnUiThread(() -> Utils.showToast(MainActivity.this, message));
        }
    }
}
