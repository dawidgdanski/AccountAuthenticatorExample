package com.authenticator.account.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.authenticator.account.R;
import com.authenticator.account.authentication.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private AccountManager accountManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        accountManager = AccountManager.get(this);
    }

    @OnClick(R.id.add_account)
    void onAddAccountButtonClick(View view) {
        addNewAccount(Constants.ACCOUNT_TYPE, Constants.Access.AUTH_TOKEN_FULL_ACCESS);
    }

    @OnClick(R.id.get_authtoken)
    void onObtainAuthenticationTokenButtonClick(View view) {
        showAccountsPicker((dialog, which) -> {
            final Account account = getAccountsByType()[which];
            showExistingAccountAuthToken(account, Constants.Access.AUTH_TOKEN_FULL_ACCESS);
        });
    }

    @OnClick(R.id.get_authtoken_by_features)
    void onGetAuthenticationTokenByFeaturesButtonClick(View view) {
        addAccountIfNeededAndShowAuthToken(Constants.ACCOUNT_TYPE, Constants.Access.AUTH_TOKEN_FULL_ACCESS);
    }

    @OnClick(R.id.invalidate_auth_token)
    void onInvalidateAuthTokenButtonClick(View view) {
        showAccountsPicker((dialog, which) -> {
            final Account account = getAccountsByType()[which];
            invalidateAuthToken(account, Constants.Access.AUTH_TOKEN_FULL_ACCESS);
        });
    }

    @OnClick(R.id.remove_account)
    void onRemoveAccountButtonClick(View view) {
        showAccountsPicker((dialog, which) -> {
            final Account account = getAccountsByType()[which];
            removeAccount(account);
        });
    }

    private void removeAccount(final Account account) {
        accountManager.removeAccount(account, future -> {
            try {
                future.getResult();
                showMessage(getString(R.string.account_removed_successfully, account.name));
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage());
            }
        },
                null);
    }

    private void addNewAccount(String accountType, String authTokenType) {
        accountManager.addAccount(accountType,
                authTokenType,
                null,
                null,
                this,
                future1 -> {
                    try {
                        future1.getResult();
                        showMessage(getString(R.string.account_created_successfully));
                    } catch (Exception e) {
                        showMessage(e.getMessage());
                    }
                },
                null);

    }

    private void addAccountIfNeededAndShowAuthToken(final String accountType, final String authTokenType) {
        accountManager.getAuthTokenByFeatures(accountType,
                authTokenType,
                null,
                this,
                null,
                null,
                future1 -> {
                    try {
                        Bundle data = future1.getResult();
                        final String authToken = data.getString(AccountManager.KEY_AUTHTOKEN);
                        showMessage(getString(R.string.authtoken, authToken));
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessage(e.getMessage());
                    }
                }, null);


    }

    private void showAccountsPicker(final DialogInterface.OnClickListener listener) {
        final String[] accountsNames = getAccountsNamesByType();
        if (accountsNames.length == 0) {
            showMessage(getString(R.string.no_accounts_created));
        } else {
            final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountsNames);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.pick_account))
                    .setAdapter(adapter, listener)
                    .create()
                    .show();
        }
    }

    private void showExistingAccountAuthToken(final Account account, final String authTokenType) {
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, authTokenType, null, this, null, null);

        new Thread(() -> {
            try {
                Bundle bnd = future.getResult();

                final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                showMessage(authtoken != null ? getString(R.string.authtoken, authtoken)
                        : getString(R.string.error));
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage());
            }
        }).start();
    }

    private void invalidateAuthToken(final Account account, final String authTokenType) {
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                authTokenType,
                null,
                this,
                null,
                null);
        new Thread(() -> {
            try {
                Bundle result = future.getResult();
                final String authtoken = result.getString(AccountManager.KEY_AUTHTOKEN);
                accountManager.invalidateAuthToken(account.type, authtoken);
                showMessage(getString(R.string.account_invalidated, account.name));
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage());
            }
        }).start();
    }

    private void showMessage(final String message) {
        if (!TextUtils.isEmpty(message)) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show());
        }
    }

    private String[] getAccountsNamesByType() {
        Account[] availableAccounts = getAccountsByType();

        String[] names = new String[availableAccounts.length];

        int index = 0;
        for (final Account account : availableAccounts) {
            names[index++] = account.name;
        }

        return names;
    }

    private Account[] getAccountsByType() {
        return accountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
    }
}
