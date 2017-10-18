package com.example.skarwa.letmeethappen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.viewholder.EventViewHolder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.skarwa.letmeethappen.R.id.imgAlert;
import static com.example.skarwa.letmeethappen.R.id.ivProfileImage;
import static com.example.skarwa.letmeethappen.R.id.tvDate;
import static com.example.skarwa.letmeethappen.R.id.tvEventName;

/**
 * Created by skarwa on 10/17/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private static final String TAG = "EventAdapter";

    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<String> mEventIds = new ArrayList<>();
    private List<Event> mEvents = new ArrayList<>();

    public EventAdapter(Context context,DatabaseReference ref) {
        this.mContext = context;
        this.mDatabaseReference = ref;

        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new event has been added, add it to the displayed list
                Event event = dataSnapshot.getValue(Event.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                mEventIds.add(dataSnapshot.getKey());
                mEvents.add(event);
                notifyItemInserted(mEvents.size() - 1);
                // [END_EXCLUDE]
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A event has changed, use the key to determine if we are displaying this
                // event and if so displayed the changed event.
                Event newEvent = dataSnapshot.getValue(Event.class);
                String eventKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int eventIndex = mEventIds.indexOf(eventKey);
                if (eventIndex > -1) {
                    // Replace with the new data
                    mEvents.set(eventIndex, newEvent);

                    // Update the RecyclerView
                    notifyItemChanged(eventIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + eventKey);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A event has changed, use the key to determine if we are displaying this
                // event and if so remove it.
                String eventKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int eventIndex = mEventIds.indexOf(eventKey);
                if (eventIndex > -1) {
                    // Remove data from the list
                    mEventIds.remove(eventIndex);
                    mEvents.remove(eventIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(eventIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + eventKey);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A event has changed position, use the key to determine if we are
                // displaying this event and if so move it.
                Event movedEvent = dataSnapshot.getValue(Event.class);
                String eventKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postEvents:onCancelled", databaseError.toException());
                Toast.makeText(mContext, "Failed to load events.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        ref.addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        final Event event = mEvents.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // handle click of view
               // ((OnEventClickListener)mContext).onEventClick(event);
            }
        });

        holder.bindToEvent(event,mContext);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}
