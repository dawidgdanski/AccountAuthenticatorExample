package com.authenticator.account.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService;
import android.support.v4.app.NotificationCompat;

import com.authenticator.account.R;
import com.authenticator.account.service.AuthorizationService;
import com.authenticator.account.ui.AuthenticatorActivity;

public class AuthorizationReceiver extends BroadcastReceiver {

    public static final String BROADCAST = AuthorizationService.class.getName() + "BROADCAST";

    public static final int AUTHORIZATION_NOTIFICATION_ID = 2322;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(AUTHORIZATION_NOTIFICATION_ID, createNotification(context, intent.getExtras()));
    }

    private Notification createNotification(final Context context, final Bundle arguments) {
        final Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtras(arguments);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        return new NotificationCompat.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.authorization_completed))
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(context.getString(R.string.user_authorized))
                .setContentIntent(pendingIntent)
                .setContentText("Yo")
                .build();
    }
}
