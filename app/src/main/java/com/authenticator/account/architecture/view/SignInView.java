package com.authenticator.account.architecture.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

public interface SignInView {
    @NonNull
    String getUsername();
    @NonNull
    String getPassword();
    @NonNull
    String getAuthTokenType();

    void onErrorOccured(@NonNull String message);

    void onUserSignedIn(@NonNull Bundle result);

    void setResult(int result, Intent data);

    boolean isNewAccountRequested();
}
