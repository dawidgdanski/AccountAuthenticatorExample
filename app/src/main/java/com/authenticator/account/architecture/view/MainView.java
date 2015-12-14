package com.authenticator.account.architecture.view;

import android.accounts.Account;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface MainView {


    void onAccountAdded();

    void onErrorOccured(@NonNull String message);

    void onRequestedTokenObtained(@Nullable String authToken);

    void onAccountRemoved(@NonNull Account account);

    void onAuthTokenLoaded(@NonNull String authtoken);

    void onAuthTokenInvalidated(@NonNull Account account);

    void onNoAccountsCreated();
}
