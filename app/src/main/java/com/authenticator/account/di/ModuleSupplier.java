package com.authenticator.account.di;

import com.authenticator.account.di.module.ApplicationModule;
import com.authenticator.account.di.module.AuthenticationModule;
import com.authenticator.account.di.module.BroadcastModule;
import com.authenticator.account.di.module.HttpModule;
import com.authenticator.account.di.module.MemoryLeakDetectionModule;

public interface ModuleSupplier {
    ApplicationModule provideApplicationModule();
    AuthenticationModule provideAuthenticationModule();
    BroadcastModule provideBroadcastModule();
    HttpModule provideHttpModule();
    MemoryLeakDetectionModule provideMemoryLeakDetectionModule();
}
