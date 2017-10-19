package com.example.skarwa.letmeethappen.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skarwa.letmeethappen.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.example.skarwa.letmeethappen.utils.Constants.USER_EVENTS;

public class PendingEventFragment extends EventsListFragment {
    private static final String TAG = "PendingEventFragment";

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(USER_EVENTS)
                .child(getUid())
                .orderByChild("eventStatus")
                .equalTo("PENDING");
        return query;
    }
}
