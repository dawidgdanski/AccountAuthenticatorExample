package com.authenticator.account.di.module;

import com.authenticator.account.authentication.AuthenticationProvider;
import com.authenticator.account.authentication.ParseAuthenticationProvider;
import com.authenticator.account.di.Qualifiers;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthenticationModule {

    @Provides
    @Singleton
    @Named(Qualifiers.PARSE_AUTHENTICATION_PROVIDER)
    public AuthenticationProvider provideParseAuthenticationProvider(OkHttpClient okHttpClient) {
        return new ParseAuthenticationProvider(okHttpClient);
    }

}
