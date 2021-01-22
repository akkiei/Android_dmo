package com.rdss.dontmissout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    private final Context mContext;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public void createNotification() {
        Intent intent = new Intent(mContext, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "100")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("A dummy Notification")
                .setContentIntent(pendingIntent)
                .setContentText("This is the description part of the notification")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("This is extra text here"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "100"; //getString(R.string.channel_name);
            String description = "this is dummy notification"; // getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("100", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(123, builder.build());
    }

}