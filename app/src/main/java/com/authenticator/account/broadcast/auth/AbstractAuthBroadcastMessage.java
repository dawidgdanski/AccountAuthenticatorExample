package com.authenticator.account.broadcast.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

abstract class AbstractAuthBroadcastMessage implements AuthBroadcastMessage {

    private final Bundle data;

    protected AbstractAuthBroadcastMessage(Bundle data) {
        Preconditions.checkNotNull(data, "Data is null");
        this.data = data;
    }

    @Override
    @NonNull
    public Bundle getData() {
        return data;
    }
}
