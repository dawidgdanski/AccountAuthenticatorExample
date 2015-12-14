package com.authenticator.account.di;

import android.accounts.AccountManager;

import com.authenticator.account.authentication.SimpleAuthenticator;
import com.authenticator.account.broadcast.BroadcastManager;
import com.authenticator.account.controller.BackgroundExecutor;
import com.authenticator.account.di.module.ApplicationModule;
import com.authenticator.account.di.module.AuthenticationModule;
import com.authenticator.account.di.module.BackgroundExecutorModule;
import com.authenticator.account.di.module.BroadcastModule;
import com.authenticator.account.di.module.HttpModule;
import com.authenticator.account.di.module.MemoryLeakDetectionModule;
import com.authenticator.account.service.AuthService;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Lazy;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        AuthenticationModule.class,
        BroadcastModule.class,
        HttpModule.class,
        BackgroundExecutorModule.class,
        MemoryLeakDetectionModule.class})
public interface DependencyGraph {
    void inject(SimpleAuthenticator authenticator);
    void inject(AuthService authService);

    BroadcastManager broadcastManager();
    AccountManager accountManager();
    Lazy<RefWatcher> refWatcher();
    BackgroundExecutor backgroundExecutor();
}
