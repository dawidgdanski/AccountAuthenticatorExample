package com.authenticator.account.architecture.presentation;

import android.accounts.Account;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.authenticator.account.util.Destroyable;

import java.util.List;

public interface MainPresenter extends Destroyable {

    void addNewAccount(@NonNull String access);

    void getAuthenticationTokenByFeatures(@NonNull String access);

    @NonNull
    List<String> getApplicationAccountNames();

    Account getRegisteredAccount(int index);

    void removeAccount(int which);

    void loadExistingAccountAuthToken(int which, @NonNull String access);

    void invalidateAuthToken(int which, @NonNull String access);

    void showAccountsPicker(@NonNull DialogInterface.OnClickListener clickListener);
}
