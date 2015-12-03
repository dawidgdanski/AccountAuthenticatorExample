package com.authenticator.account;

import android.app.Application;
import android.content.Context;

import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.di.ModuleProvisionContract;
import com.authenticator.account.di.module.ApplicationModule;
import com.authenticator.account.di.module.AuthenticationModule;
import com.authenticator.account.di.module.BroadcastModule;
import com.authenticator.account.di.module.HttpModule;
import com.parse.Parse;

public class AuthenticationApplication extends Application implements ModuleProvisionContract {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DependencyInjector.initialize(this);
    }

    @Override
    public ApplicationModule provideApplicationModule(AuthenticationApplication application) {
        return new ApplicationModule(application);
    }

    @Override
    public AuthenticationModule provideAuthenticationModule(AuthenticationApplication application) {
        return new AuthenticationModule();
    }

    @Override
    public BroadcastModule provideBroadcastModule(AuthenticationApplication application) {
        return new BroadcastModule();
    }

    @Override
    public HttpModule provideHttpModule(AuthenticationApplication application) {
        return new HttpModule();
    }
}
