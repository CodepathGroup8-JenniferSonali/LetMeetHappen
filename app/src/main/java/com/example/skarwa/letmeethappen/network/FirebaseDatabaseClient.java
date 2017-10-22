package com.example.skarwa.letmeethappen.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by skarwa on 10/22/17.
 */

public class FirebaseDatabaseClient {
    private AsyncHttpClient client;
    private String FIREBASE_GROUPS_DATABASE_URL = "https://let-meet-happen.firebaseio.com/groups.json";
    private String FIREBASE_USERS_DATABASE_URL = "https://let-meet-happen.firebaseio.com/users.json";
    private String FIREBASE_EVENTS_DATABASE_URL = "https://let-meet-happen.firebaseio.com/events.json";


    public FirebaseDatabaseClient() {
        this.client = new AsyncHttpClient();
    }

    // Method for accessing the events API
    public void getEvents(final RequestParams params, JsonHttpResponseHandler handler) {
        client.get(FIREBASE_EVENTS_DATABASE_URL, params, handler);
    }

    // Method for accessing the groups API
    public void getGroups(final RequestParams params, JsonHttpResponseHandler handler) {
        client.get(FIREBASE_GROUPS_DATABASE_URL, params, handler);
    }

    //method for accessing the users API
    public void getUsers(final RequestParams params, JsonHttpResponseHandler handler) {
        client.get(FIREBASE_USERS_DATABASE_URL, params, handler);
    }
}