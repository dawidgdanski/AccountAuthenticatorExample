package com.authenticator.account.broadcast.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;

public class SignInBroadcastMessage extends AbstractAuthBroadcastMessage {

    public SignInBroadcastMessage(@NonNull Bundle data) {
        super(data);
    }
}
