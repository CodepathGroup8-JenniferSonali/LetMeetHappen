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

        if (event.getEventStatus().equals(EventStatus.CONFIRMED)) {  //mark event as SUCCESS -> Becomes a PAST event
            event.setEventStatus(EventStatus.SUCCESSFUL.name());
        } else if (event.getAttendedUser().keySet().size() >= minYes && event.getEventStatus()!= EventStatus.CONFIRMED.name()) {
            event.setEventStatus(EventStatus.CONFIRMED.name());
            String[] dates = event.getEventDateOptions().keySet().toArray(new String[2]);
            Integer[] count = event.getEventDateOptions().values().toArray(new Integer[2]);
            if (count[0] > count[1]) {
                event.setEventFinalDate(dates[0]);
            } else if (count[0] < count[1]) {
                event.setEventFinalDate(dates[1]);
            } else { //equal count
                event.setEventFinalDate(dates[0]); //defaults to first option
            }
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

        mDatabase.child(USER_EVENTS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // run some code
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);

                    Date acceptByDate = DateUtils.parseDatefromString(event.getAcceptByDate());
                    if (acceptByDate != null && acceptByDate.after(today)) {
                        updateEvent(event);
                        Log.d(TAG, "Deadline for event " + event + " approached !!");
                    }
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

