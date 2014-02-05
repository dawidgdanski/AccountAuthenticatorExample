package com.authenticator.account.ui;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.authenticator.account.R;
import com.authenticator.account.auth.Auth;
import com.authenticator.account.auth.ServerAuthCallbacksFactory;
import com.authenticator.account.exception.AuthException;
import com.authenticator.account.interfaces.ServerAuthenticateCallbacks;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.sign_up_activity)
public class SignUpActivity extends RoboFragmentActivity {

    @InjectView(R.id.username)
    private EditText userNameText;

    @InjectView(R.id.password)
    private EditText passwordText;

    @InjectView(R.id.email)
    private EditText emailText;

    @InjectView(R.id.submit)
    private Button submitButton;

    @InjectView(R.id.alreadyMember)
    private TextView alreadyMemberButton;

    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        accountType = getIntent().getStringExtra(Auth.ACCOUNT_TYPE);

        setUpListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private String getUserName() {
        return userNameText.getText().toString().trim();
    }

    private String getPassword() {
        return passwordText.getText().toString().trim();
    }

    private String getEmail() {
        return emailText.getText().toString().trim();
    }

    private void setUpListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        alreadyMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void createAccount() {
        new AsyncTask<Void, Void, Intent>() {

            private String userName;
            private String email;
            private String password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                userName = getUserName();
                email = getEmail();
                password = getPassword();
            }

            @Override
            protected Intent doInBackground(Void... params) {
                final ServerAuthenticateCallbacks serverAuthenticate = ServerAuthCallbacksFactory.construct(ServerAuthenticateCallbacks.Server.PARSE_COM);
                final Intent resultIntent = new Intent();
                try {
                    String authToken = serverAuthenticate.onUserSignUp(userName, email, password, Auth.Access.AUTH_TOKEN_FULL_ACCESS);
                    resultIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
                    resultIntent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    resultIntent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
                    resultIntent.putExtra(AccountManager.KEY_PASSWORD, password);
                } catch (AuthException e) {
                    resultIntent.putExtra(AccountManager.KEY_ERROR_MESSAGE, e.getMessage());
                }

                return resultIntent;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                super.onPostExecute(intent);

                if(intent.hasExtra(AccountManager.KEY_ERROR_MESSAGE)) {
                    Toast.makeText(SignUpActivity.this, intent.getStringExtra(AccountManager.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

        }.execute();
    }


}
