package com.authenticator.account.ui;

import com.authenticator.account.di.DependencyGraph;

import dagger.Component;

@ActivityScope
@Component(dependencies = DependencyGraph.class,
        modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(SignUpActivity signUpActivity);
    void inject(SignInActivity signInActivity);
}
