package com.authenticator.account.broadcast;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import de.greenrobot.event.EventBus;

public class DefaultBroadcastManager implements BroadcastManager {

    private final EventBus eventBus;

    public DefaultBroadcastManager(EventBus eventBus) {
        Preconditions.checkNotNull(eventBus);
        this.eventBus = eventBus;
    }

    @Override
    public void register(@NonNull Object object) {
        Preconditions.checkNotNull(object);
        if (!eventBus.isRegistered(object)) {
            eventBus.register(object);
        }
    }

    @Override
    public void unregister(@NonNull Object object) {
        Preconditions.checkNotNull(object);
        eventBus.unregister(object);
    }

    @Override
    public void post(@NonNull Object object) {
        Preconditions.checkNotNull(object);
        eventBus.post(object);
    }
}
