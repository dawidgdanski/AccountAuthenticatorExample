package com.authenticator.account.broadcast;

import android.support.annotation.NonNull;

public interface BroadcastManager {

    void register(@NonNull Object object);

    void unregister(@NonNull Object object);

    void post(@NonNull Object object);
}
