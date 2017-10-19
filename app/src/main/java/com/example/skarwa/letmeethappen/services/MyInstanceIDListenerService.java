package com.example.skarwa.letmeethappen.services;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by jennifergodinez on 10/18/17.
 */

    public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

        @Override
        public void onTokenRefresh() {
            // Fetch updated Instance ID token and notify of changes
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

