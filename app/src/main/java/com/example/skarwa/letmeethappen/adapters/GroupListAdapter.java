package com.example.skarwa.letmeethappen.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.fragments.ViewGroupFragment;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.viewholder.EventViewHolder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by skarwa on 10/18/17.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private static final String TAG = "GroupListAdapter";

    private Context mContext;
    private DatabaseReference mUserGroupReference;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabase;

    private List<String> mGroupIds = new ArrayList<>();
    private List<Group> mGroups = new ArrayList<>();

    class GroupViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvGroupName)
        public TextView groupName;
       // public TextView bodyView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public GroupListAdapter(Context context,DatabaseReference ref) {
        this.mContext = context;
        this.mUserGroupReference = ref;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String groupKey = dataSnapshot.getKey();
                Log.d(TAG,"onChildAdded:"+dataSnapshot.getKey());
                // [START_EXCLUDE]
                // Update RecyclerView
                mGroupIds.add(dataSnapshot.getKey());

                //TODO get all groups and attach it to navigation drawer
                mDatabase.child("groups/" + groupKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Group group = snapshot.getValue(Group.class);
                        mGroups.add(group);
                        notifyItemInserted(mGroups.size() - 1);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        // ignore
                    }
                });
                // [END_EXCLUDE]
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                String groupKey = dataSnapshot.getKey();
                // [START_EXCLUDE]
                // Update RecyclerView

                //TODO get all groups and attach it to navigation drawer
              /*  mDatabase.child("groups/" + groupKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Group group = snapshot.getValue(Group.class);
                        int eventIndex = mGroupIds.indexOf(group.getId());

                        if (eventIndex > -1) {
                            // Replace with the new data
                            mGroups.set(eventIndex, group);

                            // Update the RecyclerView
                            notifyItemChanged(eventIndex);
                        }else {
                            Log.w(TAG, "onChildChanged:unknown_child:" + eventIndex);
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        // ignore
                    }
                });
                // [END_EXCLUDE]*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A event has changed, use the key to determine if we are displaying this
                // event and if so remove it.
                String eventKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int eventIndex = mGroupIds.indexOf(eventKey);
                if (eventIndex > -1) {
                    // Remove data from the list
                    mGroupIds.remove(eventIndex);
                    mGroups.remove(eventIndex);

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
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_group, parent, false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        final Group group = mGroups.get(position);

        holder.groupName.setText(group.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // handle click of view
                FragmentManager fm = ((FragmentActivity)getContext()).getSupportFragmentManager();
                ViewGroupFragment viewgroupFragment = ViewGroupFragment.newInstance(Parcels.wrap(group));
                viewgroupFragment.show(fm, "fragment_view_group");
            }
        });
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mUserGroupReference.removeEventListener(mChildEventListener);
        }
    }
}
