package com.authenticator.account.architecture.presentation;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.authenticator.account.util.Destroyable;

public interface SignInActivityPresenter extends Destroyable {

    void requestAccess();

    void notifyUserSignedUp(@NonNull Bundle data);
}
