package com.authenticator.account.ui;

import android.accounts.AccountAuthenticatorActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.authenticator.account.R;
import com.authenticator.account.architecture.presentation.DefaultSignInPresenter;
import com.authenticator.account.architecture.view.SignInView;
import com.authenticator.account.authentication.Constants;
import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.util.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AccountAuthenticatorActivity implements SignInView {

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
    @ActivityScope
    DefaultSignInPresenter presenter;

    private String authTokenType;

    private boolean isNewAccountRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticator_activity);

        bindViews();
        injectMembers();

        Bundle extras = getIntent().getExtras();

        authTokenType = extras.getString(Constants.AUTH_TOKEN_TYPE, Constants.Access.AUTH_TOKEN_FULL_ACCESS);
        isNewAccountRequested = getIntent().getBooleanExtra(Constants.IS_NEW_ACCOUNT, false);

        final String accountName = extras.getString(Constants.ACCOUNT_NAME);

        setUsername(accountName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SIGN_UP_REQUEST:
                onUserSignedUp(resultCode, data);
                break;
            default:
                throw new IllegalArgumentException(SignInActivity.class.getSimpleName() +
                        ": Unhandled request code: " +
                        requestCode);
        }

    }

    @OnClick(R.id.submit)
    void onSignInButtonClicked(View view) {
        presenter.requestAccess();

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

    void setUsername(@Nullable final String accountName) {
        usernameText.setText(accountName);
    }

    @NonNull
    @Override
    public String getUsername() {
        return usernameText.getText().toString().trim();
    }

    @NonNull
    @Override
    public String getPassword() {
        return passwordText.getText().toString().trim();
    }

    @NonNull
    @Override
    public String getAuthTokenType() {
        return authTokenType;
    }

    @Override
    public void onErrorOccured(@NonNull String message) {
        Utils.showToast(this, message);
    }

    @Override
    public void onUserSignedIn(@NonNull Bundle result) {
        setAccountAuthenticatorResult(result);
        finish();
    }

    @Override
    public boolean isNewAccountRequested() {
        return isNewAccountRequested;
    }

    protected void bindViews() {
        ButterKnife.bind(this);
    }

    protected void injectMembers() {
        DependencyInjector.activityComponent(this).inject(this);
    }

    private void onUserSignedUp(final int resultCode, final Intent intent) {
        if (resultCode == RESULT_OK) {
            presenter.notifyUserSignedUp(intent.getExtras());
        }
    }

}
