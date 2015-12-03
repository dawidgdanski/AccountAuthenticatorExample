package com.authenticator.account.di;

import com.authenticator.account.AuthenticationApplication;
import com.authenticator.account.di.module.ApplicationModule;
import com.authenticator.account.di.module.AuthenticationModule;
import com.authenticator.account.di.module.BroadcastModule;
import com.authenticator.account.di.module.HttpModule;

public interface ModuleProvisionContract {
    ApplicationModule provideApplicationModule(final AuthenticationApplication application);
    AuthenticationModule provideAuthenticationModule(final AuthenticationApplication application);
    BroadcastModule provideBroadcastModule(final AuthenticationApplication application);
    HttpModule provideHttpModule(final AuthenticationApplication application);
}
