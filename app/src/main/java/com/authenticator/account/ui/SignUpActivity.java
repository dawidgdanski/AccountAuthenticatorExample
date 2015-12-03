package com.authenticator.account.ui;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.authenticator.account.R;
import com.authenticator.account.authentication.Constants;
import com.authenticator.account.authentication.Credentials;
import com.authenticator.account.broadcast.BroadcastManager;
import com.authenticator.account.broadcast.auth.AccountCreationBroadcastMessage;
import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.di.Qualifiers;
import com.authenticator.account.authentication.AuthenticationProvider;
import com.authenticator.account.service.AuthService;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.username)
    EditText usernameText;

    @Bind(R.id.password)
    EditText passwordText;

    @Bind(R.id.email)
    EditText emailText;

    @Bind(R.id.submit)
    Button submitButton;

    @Bind(R.id.alreadyMember)
    TextView alreadyMemberButton;

    @Inject
    @Named(Qualifiers.PARSE_AUTHENTICATION_PROVIDER)
    AuthenticationProvider authenticationProvider;

    @Inject
    BroadcastManager broadcastManager;

    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        ButterKnife.bind(this);
        DependencyInjector.getGraph().inject(this);

        accountType = getIntent().getStringExtra(Constants.ACCOUNT_TYPE);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregister(this);
    }

    @OnClick(R.id.submit)
    void onSubmitButtonClick(View view) {

        final String username = getUsername();
        final String email = getEmail();
        final String password = getPassword();

        final Credentials credentials = Credentials.builder()
                .setUsername(username)
                .setEmail(email)
                .setPassword(password)
                .setAccountType(accountType)
                .build();

        broadcastManager.register(this);
        AuthService.createAccount(this, credentials);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(AccountCreationBroadcastMessage message) {
        if (isFinishing()) {
            return;
        }

        final Bundle data = message.getData();

        if (data.containsKey(AccountManager.KEY_ERROR_MESSAGE)) {
            Toast.makeText(SignUpActivity.this, data.getString(AccountManager.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
        } else {
            final Intent intent = new Intent().putExtras(data);

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick(R.id.alreadyMember)
    void onAlreadyMemberButtonClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private String getUsername() {
        return usernameText.getText().toString().trim();
    }

    private String getPassword() {
        return passwordText.getText().toString().trim();
    }

    private String getEmail() {
        return emailText.getText().toString().trim();
    }
}
