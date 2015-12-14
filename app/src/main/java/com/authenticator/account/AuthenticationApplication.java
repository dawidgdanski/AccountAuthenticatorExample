package com.authenticator.account;

import android.app.Application;
import android.content.Context;

import com.authenticator.account.di.DependencyInjector;
import com.authenticator.account.di.ModuleSupplier;
import com.authenticator.account.di.module.ApplicationModule;
import com.authenticator.account.di.module.AuthenticationModule;
import com.authenticator.account.di.module.BroadcastModule;
import com.authenticator.account.di.module.HttpModule;
import com.authenticator.account.di.module.MemoryLeakDetectionModule;
import com.authenticator.account.util.ApiUtils;
import com.squareup.leakcanary.LeakCanary;

public class AuthenticationApplication extends Application implements ModuleSupplier {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DependencyInjector.initialize(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApiUtils.installStetho(this);
    }

    @Override
    public ApplicationModule provideApplicationModule() {
        return new ApplicationModule(this);
    }

    @Override
    public AuthenticationModule provideAuthenticationModule() {
        return new AuthenticationModule();
    }

    @Override
    public BroadcastModule provideBroadcastModule() {
        return new BroadcastModule();
    }

    @Override
    public HttpModule provideHttpModule() {
        return new HttpModule();
    }

    @Override
    public MemoryLeakDetectionModule provideMemoryLeakDetectionModule() {
        return new MemoryLeakDetectionModule(BuildConfig.DEBUG, this);
    }
}
