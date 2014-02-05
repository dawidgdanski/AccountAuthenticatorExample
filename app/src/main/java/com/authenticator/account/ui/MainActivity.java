package com.authenticator.account.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.authenticator.account.R;
import com.authenticator.account.auth.Auth;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboFragmentActivity implements View.OnClickListener {

    private AccountManager accountManager;

    @InjectView(R.id.add_account)
    private Button addAccountButton;

    @InjectView(R.id.get_authtoken)
    private Button getAuthtokenButton;

    @InjectView(R.id.get_authtoken_by_features)
    private Button getAuthtokenByFeaturesButton;

    @InjectView(R.id.invalidate_auth_token)
    private Button invalidateAuthTokenButton;

    @InjectView(R.id.remove_account)
    private Button removeAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = AccountManager.get(this);
        setUpListeners();
    }

    private void setUpListeners() {
        addAccountButton.setOnClickListener(this);
        getAuthtokenButton.setOnClickListener(this);
        getAuthtokenByFeaturesButton.setOnClickListener(this);
        invalidateAuthTokenButton.setOnClickListener(this);
        removeAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.add_account:
                addNewAccount(Auth.ACCOUNT_TYPE, Auth.Access.AUTH_TOKEN_FULL_ACCESS);
                break;

            case R.id.get_authtoken:
                showAccountsPicker(new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                final Account account = getAccountsByType()[which];
                                                showExistingAccountAuthToken(account, Auth.Access.AUTH_TOKEN_FULL_ACCESS);
                                        }
                                    });
                break;

            case R.id.get_authtoken_by_features:
                addAccountIfNeededAndShowAuthToken(Auth.ACCOUNT_TYPE, Auth.Access.AUTH_TOKEN_FULL_ACCESS);
                break;

            case R.id.invalidate_auth_token:
                showAccountsPicker(new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final Account account = getAccountsByType()[which];
                                            invalidateAuthToken(account, Auth.Access.AUTH_TOKEN_FULL_ACCESS);
                                        }
                                    });
                break;

            case R.id.remove_account:
                showAccountsPicker(new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final Account account = getAccountsByType()[which];
                                            removeAccount(account);
                                        }
                                    });
                break;
            default:
                break;
        }
    }

    private void removeAccount(final Account account) {
        accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
            @Override
            public void run(AccountManagerFuture<Boolean> future) {
                try {
                    future.getResult();
                    showMessage(getString(R.string.account_removed_successfully, account.name));
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        },
        null);
    }

    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = accountManager.addAccount(accountType,
                                                                              authTokenType,
                                                                              null,
                                                                              null,
                                                                              this,
            new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    try {
                        future.getResult();
                        showMessage(getString(R.string.account_created_successfully));
                    } catch(Exception e) {
                        showMessage(e.getMessage());
                    }
                }
            },
            null);

    }

    private void addAccountIfNeededAndShowAuthToken(final String accountType, final String authTokenType) {
        final AccountManagerFuture<Bundle> future = accountManager.getAuthTokenByFeatures(accountType,
                                                                                          authTokenType,
                                                                                          null,
                                                                                          this,
                                                                                          null,
                                                                                          null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle data = future.getResult();
                            final String authToken = data.getString(AccountManager.KEY_AUTHTOKEN);
                            showMessage(getString(R.string.authtoken, authToken));
                        } catch(Exception e) {
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }
                    }
                }, null);


    }

    private void showAccountsPicker(final DialogInterface.OnClickListener listener) {
        final String[] accountsNames = getAccountsNamesByType();
        if(accountsNames.length == 0) {
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();

                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    showMessage(authtoken != null ? getString(R.string.authtoken, authtoken)
                                                  : getString(R.string.error));
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle result = future.getResult();
                    final String authtoken = result.getString(AccountManager.KEY_AUTHTOKEN);
                    accountManager.invalidateAuthToken(account.type, authtoken);
                    showMessage(getString(R.string.account_invalidated, account.name));
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }).start();
    }

    private void showMessage(final String message) {
        if(!TextUtils.isEmpty(message)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private String[] getAccountsNamesByType() {
        Account[] availableAccounts = getAccountsByType();

        String[] names = new String[availableAccounts.length];

        int index = 0;
        for(final Account account : availableAccounts) {
            names[index++] = account.name;
        }

        return names;
    }

    private Account[] getAccountsByType() {
        return accountManager.getAccountsByType(Auth.ACCOUNT_TYPE);
    }

    private enum Action {
        SHOW_AUTHTOKEN,
        INVALIDATE_AUTHTOKEN,
        REMOVE_ACCOUNT
    }
}
