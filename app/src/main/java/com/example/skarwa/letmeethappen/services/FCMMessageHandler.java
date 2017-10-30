package com.example.skarwa.letmeethappen.services;

/**
 * Created by jennifergodinez on 10/18/17.
 */

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.activities.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMMessageHandler extends FirebaseMessagingService {
    public static final int MESSAGE_NOTIFICATION_ID = 435340;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String from = remoteMessage.getFrom();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        createNotification(notification);
    }

    // Creates notification based on title and body received
    private void createNotification(RemoteMessage.Notification notification) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notification.getTitle())
                .setContentText(notification.getBody());
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

        launchSomeActivity(context, "");
    }

    private void launchSomeActivity(Context context, String datavalue) {
        Intent pupInt = new Intent(context, LoginActivity.class);
        pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //pupInt.putExtra("data", datavalue);

        context.getApplicationContext().startActivity(pupInt);
    }
}
