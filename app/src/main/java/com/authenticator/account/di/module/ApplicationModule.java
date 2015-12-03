package com.authenticator.account.di.module;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.res.Resources;

import com.authenticator.account.AuthenticationApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final AuthenticationApplication application;

    public ApplicationModule(AuthenticationApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public AccountManager provideAccountManager() {
        return AccountManager.get(application);
    }

    @Provides
    @Singleton
    public AuthenticationApplication provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    public Resources provideResources() {
        return application.getResources();
    }
}
