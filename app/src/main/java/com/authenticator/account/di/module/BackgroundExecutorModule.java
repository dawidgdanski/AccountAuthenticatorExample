package com.authenticator.account.di.module;

import com.authenticator.account.controller.BackgroundExecutor;
import com.authenticator.account.controller.DefaultBackgroundExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BackgroundExecutorModule {

    @Provides
    @Singleton
    BackgroundExecutor provideBackgroundExecutor() {
        return new DefaultBackgroundExecutor();
    }

}
