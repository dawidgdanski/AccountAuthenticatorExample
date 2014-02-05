package com.authenticator.account.ui;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.authenticator.account.R;
import com.authenticator.account.auth.Auth;
import com.authenticator.account.receiver.AuthorizationReceiver;
import com.authenticator.account.service.AuthorizationService;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private AccountManager accountManager;
    private String authTokenType;

    private final int SIGN_UP_REQUEST = 1;

    private final BroadcastReceiver authorizationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            onSigningInFinished(intent);
        }
    };

    private final IntentFilter authResponseIntentFilter;

    {
        authResponseIntentFilter = new IntentFilter(AuthorizationReceiver.BROADCAST);
        authResponseIntentFilter.setPriority(2);
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.authenticator_activity);

        accountManager = AccountManager.get(this);

        Intent intent = getIntent();

        final String accountName = intent.getStringExtra(Auth.ACCOUNT_NAME);
        authTokenType = intent.getStringExtra(Auth.AUTH_TOKEN_TYPE);

        if(authTokenType == null) {
            authTokenType = Auth.Access.AUTH_TOKEN_FULL_ACCESS;
        }

        if(accountName != null) {
            setUserName(accountName);
        }

        setUpListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            unregisterReceiver(authorizationReceiver);
        } catch(RuntimeException e) { }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case SIGN_UP_REQUEST:
                onUserSignedUp(resultCode, data);
                break;
            default:
                throw new IllegalArgumentException(AuthenticatorActivity.class.getSimpleName() +
                                                   ": Unhandled request code: " +
                                                   requestCode);
        }

    }

    public void setUserName(final String accountName) {
        ((EditText)findViewById(R.id.username)).setText(accountName);
    }

    public String getUserName() {
        return ((EditText)findViewById(R.id.username)).getText().toString().trim();
    }

    private String getPassword() {
        return ((EditText)findViewById(R.id.password)).getText().toString().trim();
    }

    private void setUpListeners() {
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUp();
            }
        });
    }

    private void onSubmit() {
        final String userName = getUserName();
        final String password = getPassword();
        final String accountType = getIntent().getStringExtra(Auth.ACCOUNT_TYPE);

        final Intent serviceIntent = new Intent(this, AuthorizationService.class);
        serviceIntent.putExtra(Auth.USER_NAME, userName);
        serviceIntent.putExtra(Auth.PASSWORD, password);
        serviceIntent.putExtra(Auth.ACCOUNT_TYPE, accountType);
        serviceIntent.putExtra(Auth.AUTH_TOKEN_TYPE, authTokenType);

        registerReceiver(authorizationReceiver, authResponseIntentFilter);
        startService(serviceIntent);
    }

    private void onSignUp() {
        final Intent signUpIntent = new Intent(this, SignUpActivity.class);
        Bundle arguments = getIntent().getExtras();
        if(arguments != null) {
            signUpIntent.putExtras(arguments);
        }
        startActivityForResult(signUpIntent, SIGN_UP_REQUEST);
    }

    private void onSigningInFinished(final Intent resultIntent) {

        if(resultIntent.hasExtra(AccountManager.KEY_ERROR_MESSAGE)) {
            onSignInErrorHandle(resultIntent.getStringExtra(AccountManager.KEY_ERROR_MESSAGE));
        } else {
            addAccountOrUpdateWithPassword(resultIntent);
            setAccountAuthenticatorResult(resultIntent.getExtras());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        try {
            unregisterReceiver(authorizationReceiver);
        } catch(RuntimeException e) { }
    }

    private void onSignInErrorHandle(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void addAccountOrUpdateWithPassword(final Intent resultIntent) {
        String accountName = resultIntent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = resultIntent.getStringExtra(AccountManager.KEY_PASSWORD);
        String accountType = resultIntent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

        boolean isAddingNewAccount = getIntent().getBooleanExtra(Auth.IS_NEW_ACCOUNT, false);

        final Account account = new Account(accountName, accountType);

        if(isAddingNewAccount) {
            String authToken = resultIntent.getStringExtra(AccountManager.KEY_AUTHTOKEN);

            accountManager.addAccountExplicitly(account, accountPassword, null);
            accountManager.setAuthToken(account, authTokenType, authToken);
        } else {
            accountManager.setPassword(account, accountPassword);
        }
    }

    private void onUserSignedUp(final int resultCode, final Intent intent) {
        if(resultCode == RESULT_OK) {
            onSigningInFinished(intent);
        }
    }

}
