package com.authenticator.account.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.authenticator.account.R;
import com.authenticator.account.architecture.presentation.DefaultSignUpPresenter;
import com.authenticator.account.architecture.presentation.SignUpPresenter;
import com.authenticator.account.architecture.view.SignUpView;
import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.util.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

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
    @ActivityScope
    DefaultSignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        bindViews();
        injectMembers();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @OnClick(R.id.submit)
    void onSubmitButtonClick(View view) {
        presenter.createAccount();
    }

    @OnClick(R.id.alreadyMember)
    void onAlreadyMemberButtonClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public String getUsername() {
        return usernameText.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return passwordText.getText().toString().trim();
    }

    @Override
    public String getEmail() {
        return emailText.getText().toString().trim();
    }

    @Override
    public void showToast(@Nullable String message) {
        if (!TextUtils.isEmpty(message)) {
            Utils.showToast(this, message);
        }
    }

    protected void bindViews() {
        ButterKnife.bind(this);
    }

    protected void injectMembers() {
        DependencyInjector.activityComponent(this).inject(this);
    }
}
