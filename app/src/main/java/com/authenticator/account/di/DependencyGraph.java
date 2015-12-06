package com.authenticator.account.di;

import com.authenticator.account.authentication.SimpleAuthenticator;
import com.authenticator.account.di.module.ApplicationModule;
import com.authenticator.account.di.module.AuthenticationModule;
import com.authenticator.account.di.module.BroadcastModule;
import com.authenticator.account.di.module.HttpModule;
import com.authenticator.account.service.AuthService;
import com.authenticator.account.ui.AuthenticatorActivity;
import com.authenticator.account.ui.MainActivity;
import com.authenticator.account.ui.SignUpActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        AuthenticationModule.class,
        BroadcastModule.class,
        HttpModule.class})
public interface DependencyGraph {
        void inject(SimpleAuthenticator authenticator);
        void inject(AuthService authService);
        void inject(SignUpActivity signUpActivity);
        void inject(AuthenticatorActivity activity);
        void inject(MainActivity activity);
}
