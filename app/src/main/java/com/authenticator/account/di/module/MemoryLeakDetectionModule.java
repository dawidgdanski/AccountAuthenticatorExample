package com.authenticator.account.di.module;

import android.app.Application;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MemoryLeakDetectionModule {

    private final boolean isDebug;
    private final Application application;

    public MemoryLeakDetectionModule(final boolean isDebug, @NonNull final Application application) {
        this.isDebug = isDebug;
        this.application = application;
    }

    @Provides
    @Singleton
    RefWatcher provideReferenceWatcher() {
        return isDebug ? LeakCanary.install(application) : RefWatcher.DISABLED;
    }

}
