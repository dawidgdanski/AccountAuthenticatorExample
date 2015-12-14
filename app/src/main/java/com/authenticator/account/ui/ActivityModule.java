package com.authenticator.account.ui;

import android.app.Activity;
import android.os.Bundle;

import com.authenticator.account.architecture.view.MainView;
import com.authenticator.account.architecture.view.SignInView;
import com.authenticator.account.architecture.view.SignUpView;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    Bundle provideExtras() {
        return activity.getIntent().getExtras();
    }

    @Provides
    @ActivityScope
    MainView provideMainView() {
        return (MainView) activity;
    }

    @Provides
    @ActivityScope
    SignInView provideSignInView() {
        return (SignInView) activity;
    }

    @Provides
    @ActivityScope
    SignUpView provideSignUpView() {
        return (SignUpView) activity;
    }
}
