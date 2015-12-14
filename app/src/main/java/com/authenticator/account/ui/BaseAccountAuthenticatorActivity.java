package com.authenticator.account.ui;

import android.accounts.AccountAuthenticatorActivity;

import com.google.common.base.Preconditions;

abstract class BaseAccountAuthenticatorActivity extends AccountAuthenticatorActivity {

    private ActivityComponent component;

    public ActivityComponent getComponent() {
        Preconditions.checkNotNull(component);
        return component;
    }

    public void setComponent(ActivityComponent component) {
        Preconditions.checkNotNull(component);
        this.component = component;
    }
}
