package com.authenticator.account.util;

import android.app.Application;
import android.support.annotation.NonNull;

import com.authenticator.account.BuildConfig;
import com.facebook.stetho.Stetho;

public final class Utils {

    private Utils() {

    }

    public static void installStetho(@NonNull Application application) {
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(application)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(application))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(application))
                            .build()
            );
        }
    }

}
