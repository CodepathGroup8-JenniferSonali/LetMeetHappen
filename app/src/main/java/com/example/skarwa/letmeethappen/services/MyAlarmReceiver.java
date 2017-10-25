package com.example.skarwa.letmeethappen.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static com.example.skarwa.letmeethappen.utils.Constants.USER_ID;

/**
 * Created by skarwa on 10/24/17.
 */

public class MyAlarmReceiver extends BroadcastReceiver {
        public static final int REQUEST_CODE = 12345;
        public static final String ACTION = "com.example.skarwa.letmeethappen.services";

        // Triggered by the Alarm periodically (starts the service to run task)
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, MyEventTrackingService.class);

            i.putExtra(USER_ID,intent.getStringExtra(USER_ID));
            context.startService(i);
        }
}

