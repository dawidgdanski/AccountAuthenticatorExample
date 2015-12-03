package com.authenticator.account.ui;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.authenticator.account.R;
import com.authenticator.account.authentication.Constants;
import com.authenticator.account.authentication.Credentials;
import com.authenticator.account.broadcast.BroadcastManager;
import com.authenticator.account.broadcast.auth.SignInBroadcastMessage;
import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.service.AuthService;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private static final int SIGN_UP_REQUEST = 1;

    @Bind(R.id.username)
    EditText usernameText;

    @Bind(R.id.password)
    EditText passwordText;

    @Bind(R.id.submit)
    Button submitButton;

    @Bind(R.id.sign_up)
    TextView signUpLabel;

    @Inject
    AccountManager accountManager;

    @Inject
    BroadcastManager broadcastManager;

    private String authTokenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticator_activity);
        ButterKnife.bind(this);
        DependencyInjector.getGraph().inject(this);

        Bundle extras = getIntent().getExtras();

        authTokenType = extras.getString(Constants.AUTH_TOKEN_TYPE, Constants.Access.AUTH_TOKEN_FULL_ACCESS);

        final String accountName = extras.getString(Constants.ACCOUNT_NAME);

        setUsername(accountName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SIGN_UP_REQUEST:
                onUserSignedUp(resultCode, data);
                break;
            default:
                throw new IllegalArgumentException(AuthenticatorActivity.class.getSimpleName() +
                        ": Unhandled request code: " +
                        requestCode);
        }

    }

    @OnClick(R.id.submit)
    void onSignInButtonClicked(View view) {
        final String username = getUsername();
        final String password = getPassword();
        final String accountType = getIntent().getStringExtra(Constants.ACCOUNT_TYPE);

        final Credentials credentials = Credentials.builder()
                .setUsername(username)
                .setPassword(password)
                .setAccountType(accountType)
                .setAuthTokenType(authTokenType)
                .build();

        broadcastManager.register(this);
        AuthService.signIn(this, credentials);
    }

    @OnClick(R.id.sign_up)
    void onSignUpButtonClicked(View view) {
        final Intent signUpIntent = new Intent(this, SignUpActivity.class);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            signUpIntent.putExtras(arguments);
        }
        startActivityForResult(signUpIntent, SIGN_UP_REQUEST);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(SignInBroadcastMessage signInBroadcastMessage) {
        onSigningInFinished(signInBroadcastMessage.getData());
    }

    void setUsername(final String accountName) {
        usernameText.setText(accountName);
    }

    String getUsername() {
        return usernameText.getText().toString().trim();
    }

    String getPassword() {
        return passwordText.getText().toString().trim();
    }

    private void onSigningInFinished(final Bundle result) {

        if (result.containsKey(AccountManager.KEY_ERROR_MESSAGE)) {
            onSignInErrorHandle(result.getString(AccountManager.KEY_ERROR_MESSAGE));
        } else {
            addAccountOrUpdateWithPassword(result);
            setAccountAuthenticatorResult(result);
            Intent resultIntent = new Intent().putExtras(result);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private void onSignInErrorHandle(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void addAccountOrUpdateWithPassword(final Bundle bundle) {
        String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = bundle.getString(AccountManager.KEY_PASSWORD);
        String accountType = bundle.getString(AccountManager.KEY_ACCOUNT_TYPE);

        boolean isAddingNewAccount = getIntent().getBooleanExtra(Constants.IS_NEW_ACCOUNT, false);

        final Account account = new Account(accountName, accountType);

        if (isAddingNewAccount) {
            String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

            accountManager.addAccountExplicitly(account, accountPassword, null);
            accountManager.setAuthToken(account, authTokenType, authToken);
        } else {
            accountManager.setPassword(account, accountPassword);
        }
    }

    private void onUserSignedUp(final int resultCode, final Intent intent) {
        if (resultCode == RESULT_OK) {
            onSigningInFinished(intent.getExtras());
        }
    }

}
