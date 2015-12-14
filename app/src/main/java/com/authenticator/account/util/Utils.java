package com.authenticator.account.util;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

public final class Utils {

    private static boolean thisAMainThread;

    private Utils() { }

    public static void showToast(final @NonNull Context context, final @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isThisAMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
