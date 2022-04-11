package com.example.apphost;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

public class MessageReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "HostAPP1010";
    public static int mNotifID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("welkinli", "onReceive: receive!!!!!");
        if (intent.getAction() != null && intent.getAction().equals("com.example.apphost.MY_BROADCAST")) {
            String msg = intent.getStringExtra("message");
            createNotificationChannel(context);
            showNotification(context,msg);
        }
    }

    private void showNotification(Context context,String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("welkinli's")
                .setContentText(msg)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.hostapp))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(mNotifID++, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "From Target APP";
            String description = "Host APP desc";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
