package com.authenticator.account.di.module;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HttpModule {

    private static final int CACHE_SIZE = 5 * 1024 * 1024;

    private static final int CONNECTION_TIMEOUT = 10;

    private static final int READ_TIMEOUT = 5;

    private static final int WRITE_TIMEOUT = 5;

    private static final TimeUnit TIMEOUT_TIME_UNIT = TimeUnit.SECONDS;

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Context context) {
        final OkHttpClient httpClient = new OkHttpClient();

        final Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);

        httpClient.setCache(cache);
        httpClient.setConnectTimeout(CONNECTION_TIMEOUT, TIMEOUT_TIME_UNIT);
        httpClient.setReadTimeout(READ_TIMEOUT, TIMEOUT_TIME_UNIT);
        httpClient.setWriteTimeout(WRITE_TIMEOUT, TIMEOUT_TIME_UNIT);

        return httpClient;
    }

}
