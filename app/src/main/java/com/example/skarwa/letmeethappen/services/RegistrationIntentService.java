package com.example.skarwa.letmeethappen.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.skarwa.letmeethappen.R;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by jennifergodinez on 10/18/17.
 */
public class RegistrationIntentService extends IntentService {
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String FCM_TOKEN = "FCMToken";


    // abbreviated tag name
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Make a call to Instance API
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken();
            Log.d(TAG, "FCM Registration Token: " + token);
            // save token
            sharedPreferences.edit().putString(FCM_TOKEN, token).apply();

            // pass along this data
            sendRegistrationToServer(token);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();

        }
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();

    }
}
