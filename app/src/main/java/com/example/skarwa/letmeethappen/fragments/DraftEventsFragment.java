package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.skarwa.letmeethappen.models.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by jennifergodinez on 10/2/17.
 */

public class DraftEventsFragment extends EventsListFragment {

    private static final String TAG = "DraftEventsFragment";

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .orderByChild("eventStatus")
                .equalTo("DRAFT");
        return query;
    }
}
