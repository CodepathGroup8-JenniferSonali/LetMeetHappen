package com.example.skarwa.letmeethappen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by skarwa on 10/22/17.
 */

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.GroupMemberHolder> {

    private static final String TAG = "GroupMembersAdapter";

    private Context mContext;
    private DatabaseReference mGroupReference;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabase;

    private List<String> mUserIds = new ArrayList<>();
    private List<User> mUsers = new ArrayList<>();

    class GroupMemberHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvGroupMemberName)
        public TextView groupMember;

        @BindView(R.id.ivProfileURL)
        public ImageView memberPic;

        public GroupMemberHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public GroupMembersAdapter(Context context,DatabaseReference ref) {
        this.mContext = context;
        this.mGroupReference = ref;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String userKey = dataSnapshot.getKey();
                Log.d(TAG,"onChildAdded:"+dataSnapshot.getKey());
                // [START_EXCLUDE]
                // Update RecyclerView
                mUserIds.add(dataSnapshot.getKey());

                //TODO get all groups and attach it to navigation drawer
                mDatabase.child(Constants.USERS_ENDPOINT +"/"+ userKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        mUsers.add(user);
                        notifyItemInserted(mUsers.size() - 1);
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
                int eventIndex = mUserIds.indexOf(eventKey);
                if (eventIndex > -1) {
                    // Remove data from the list
                    mUserIds.remove(eventIndex);
                    mUsers.remove(eventIndex);

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
    public GroupMemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_group_member, parent, false);

        return new GroupMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupMemberHolder holder, int position) {
        final User user = mUsers.get(position);

        holder.groupMember.setText(user.getDisplayName());
        Glide.with(getContext()).load(user.getProfilePicUrl())
                .placeholder(R.drawable.ic_host_placeholder)
                .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 10))
                .override(100, 200)
                .into(holder.memberPic);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mGroupReference.removeEventListener(mChildEventListener);
        }
    }
}
