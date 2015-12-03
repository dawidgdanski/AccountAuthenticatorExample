package com.authenticator.account.di.module;

import com.authenticator.account.BuildConfig;
import com.authenticator.account.broadcast.BroadcastManager;
import com.authenticator.account.broadcast.DefaultBroadcastManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module
public class BroadcastModule {

    @Provides
    @Singleton
    public BroadcastManager provideBroadcastManager() {
        final boolean isDebugBuild = BuildConfig.DEBUG;

        final EventBus eventBus = EventBus.builder()
                .logNoSubscriberMessages(isDebugBuild)
                .logSubscriberExceptions(isDebugBuild)
                .build();

        return new DefaultBroadcastManager(eventBus);
    }
}
