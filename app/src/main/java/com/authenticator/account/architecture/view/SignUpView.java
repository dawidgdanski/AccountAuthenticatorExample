package com.authenticator.account.architecture.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface SignUpView {

    @NonNull
    String getUsername();

    @NonNull
    String getPassword();

    @NonNull
    String getEmail();

    boolean isFinishing();

    void showToast(@Nullable String message);

    void finish();

    void setResult(int result, Intent intent);
}
