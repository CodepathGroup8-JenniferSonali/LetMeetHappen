package com.example.skarwa.letmeethappen.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.network.FirebaseDatabaseClient;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.DateUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static android.app.SearchManager.QUERY;
import static android.content.ContentValues.TAG;
import static com.example.skarwa.letmeethappen.utils.Constants.EVENTS_ENDPOINT;
import static com.example.skarwa.letmeethappen.utils.Constants.USERS_ENDPOINT;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_EVENTS;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_ID;

/**
 * Created by skarwa on 10/22/17.
 */

public class MyEventTrackingService extends IntentService {
    DatabaseReference mDatabase;
    DatabaseReference mEventsDatabase;
    private ValueEventListener eventListener;
    FirebaseDatabaseClient mClient;


    // Must create a default constructor
    public MyEventTrackingService() {
        // Used to name the worker thread, important only for debugging.
        super("event-tracking-service");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mEventsDatabase = mDatabase.child(Constants.EVENTS_ENDPOINT);
        // [END initialize_database_ref]
    }


    private void updateEvent(Event event) {
        int minYes = event.getMinAcceptance();

        Map<String, Object> childUpdates = new HashMap<>();

        if (event.getAttendedUser().keySet().size() >= minYes) {
            event.setEventStatus(EventStatus.CONFIRMED.name());
        } else {
            event.setEventStatus(EventStatus.CANCELLED.name());
        }

        childUpdates.put("/" + EVENTS_ENDPOINT + "/" + event.getId(), event);

        for (String userId : event.getGroup().getMembers().keySet()) {
            childUpdates.put("/" + USER_EVENTS + "/" + userId + "/" + event.getId(), event);
        }

        mDatabase.updateChildren(childUpdates);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final Date today = new Date();
        String userId = intent.getStringExtra(USER_ID);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // run some code
                if (dataSnapshot.hasChild(USER_EVENTS)) {
                    //if users already exists
                    mDatabase.child(USER_EVENTS).child(USER_ID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Event event = dataSnapshot.getValue(Event.class);

                                if (event.getAcceptByDate() != null && event.getAcceptByDate().equals(DateUtils.formatDateToString(today))) {
                                    updateEvent(event);
                                    Log.d(TAG, "Deadline for event " + event + " approached !!");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to load events.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}

    /*public void fetchEvents(int page) {
        Log.d("DEBUG", " Loading Page:" + page);

        mClient.getEvents(null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private RequestParams getParams(int page) {

        RequestParams params = new RequestParams();
        //  params.put("auth", Constants.MY_DATABSE_API_KEY);
        //params.put(PAGE, page);
        /*params.put(QUERY, queryString);
        if (mBeginDate != null) {
            params.put(BEGIN_DATE, mBeginDate);
        }
        if (mSortOrder != null) {
            params.put(SORT, mSortOrder);
        }

        if (mNewsDeskValues != null && mNewsDeskValues.size() >= 1) {
            params.put(FILTER_QUERY, getNewsDeskFilterQuery());
        }
        return params;
    }*/

