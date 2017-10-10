package com.example.skarwa.letmeethappen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.skarwa.letmeethappen.R;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);


    }


    // temporary login button, will be replaced with Google button
    public void loginToRest( View v) {
        onLoginSuccess();

    }


    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    //@Override
    public void onLoginSuccess() {
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, TimelineActivity.class);
        startActivity(i);
    }

}
