package com.authenticator.account.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.authenticator.account.auth.SimpleAuthenticator;

public class AuthenticationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new SimpleAuthenticator(this).getIBinder();
    }
}
