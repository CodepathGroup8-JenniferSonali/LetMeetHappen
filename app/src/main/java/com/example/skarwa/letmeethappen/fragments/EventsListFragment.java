package com.example.skarwa.letmeethappen.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.viewholder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jennifergodinez on 10/2/17.
 */

public abstract class EventsListFragment extends Fragment implements Constants {

    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;
    LinearLayoutManager mManager;

    DatabaseReference mDatabase;
    FirebaseRecyclerAdapter<Event,EventViewHolder> mAdapter;

    public EventsListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvEvents.setLayoutManager(mManager);
        rvEvents.getItemAnimator().setChangeDuration(0);
        /*RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvEvents.addItemDecoration(itemDecoration);*/

        // Set up FirebaseRecyclerAdapter with the Query
        Query eventsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(eventsQuery, Event.class)
                .build();


       mAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(EventViewHolder viewHolder, int position, final Event model) {
                if(model != null) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            // handle click of view
                            ((OnEventClickListener)getActivity()).onEventClick(model);
                        }
                    });

                    // Bind Post to ViewHolder
                    viewHolder.bindToEvent(model,getActivity());
                }
            }

            @Override
            public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_event, parent, false);

                return new EventViewHolder(view);

            }
        };
        rvEvents.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

     public String getUid() {
         SharedPreferences shared = getActivity().getSharedPreferences(
                 USER_DETAILS, Context.MODE_PRIVATE);
         String emailId = shared.getString(USER_ID, null);
        //return FirebaseAuth.getInstance().getCurrentUser().getUid();
         return emailId;
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

}
