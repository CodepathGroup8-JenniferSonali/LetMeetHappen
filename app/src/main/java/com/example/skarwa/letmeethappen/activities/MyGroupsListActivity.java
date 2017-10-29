package com.example.skarwa.letmeethappen.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.adapters.GroupListAdapter;
import com.example.skarwa.letmeethappen.fragments.NewEventFragment;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.skarwa.letmeethappen.utils.Constants.EVENTS_ENDPOINT;
import static com.example.skarwa.letmeethappen.utils.Constants.MY_GROUPS;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_DETAILS;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_DISPLAY_NAME;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_EVENTS;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_ID;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_PROFILE_URL;

public class MyGroupsListActivity extends AppCompatActivity implements NewEventFragment.OnCreateEventClickListener {

    private static final String TAG = "MyGroupsListActivity";

    // [START declare_database_ref]
    private DatabaseReference mUserGroupReference;
    // [END declare_database_ref]
    String loggedInUserId;
    String loggedInUserDisplayName;
    String loggedInUserProfilePic;
    ArrayList<? extends Parcelable> friends;

    GroupListAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;

    SharedPreferences sharedPref;

    @BindView(R.id.rvGroups)
    RecyclerView rvGroups;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        sharedPref = this.getSharedPreferences(
                USER_DETAILS, Context.MODE_PRIVATE);

        loggedInUserId = sharedPref.getString(USER_ID,null);
        loggedInUserDisplayName =  sharedPref.getString(USER_DISPLAY_NAME,null);
        loggedInUserProfilePic = sharedPref.getString(USER_PROFILE_URL,null);
        friends = (ArrayList<? extends Parcelable>) getIntent().getParcelableArrayListExtra(Constants.FRIENDS_OBJ);


        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserGroupReference = FirebaseDatabase.getInstance().getReference().child("users/"+loggedInUserId+"/groups");
        // [END initialize_database_ref]

        getSupportActionBar().setTitle(MY_GROUPS);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();

    }

    private void initRecyclerView() {
        mAdapter = new GroupListAdapter(this, mUserGroupReference);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.scrollToPosition(0);

        rvGroups.setLayoutManager(linearLayoutManager);
        rvGroups.addItemDecoration(itemDecoration);
        rvGroups.setAdapter(mAdapter);
    }


    @Override
    public void onCreateEvent(Event event) {
        Map<String, Boolean> groupMembers = event.getGroup().getMembers();

        event.setPlannerId(loggedInUserId);
        event.setPlannerName(loggedInUserDisplayName);
        event.setHostProfileImage(loggedInUserProfilePic);
        event.addAttendedUser(loggedInUserId,true);
        Toast.makeText(this, "Saving Event...", Toast.LENGTH_SHORT).show();

        // String key = mDatabase.child(EVENTS_ENDPOINT).child(loggedInUser.)

        String key = mDatabase.child(EVENTS_ENDPOINT).push().getKey();
        event.setId(key); //setting event id as we need to link events to its ids.
        // Map<String, Object> eventValues = event.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+EVENTS_ENDPOINT+"/" + key, event);
        childUpdates.put("/"+USER_EVENTS+"/" + loggedInUserId + "/" + key, event);

        //Add event for all users who are group members
        for (String userId : groupMembers.keySet()) {
            childUpdates.put("/" + USER_EVENTS + "/" + userId + "/" +key, event);
        }

        //TODO add this to sending invites as well.

        mDatabase.updateChildren(childUpdates);

        //show the ViewEventsActivity
        Intent i = new Intent(this, ViewEventsActivity.class);
        //send user details to the next activity to fetch groups and events

        //show pending events tab as thats where the new event will get added
        i.putExtra("ShowTabIndex",2);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_groups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                //finish();
                break;
            case R.id.addGroup:
                Intent i = new Intent(getBaseContext(), NewGroupCreateActivity.class);
                i.putParcelableArrayListExtra(Constants.FRIENDS_OBJ, (ArrayList<? extends Parcelable>) friends);
                //send user details to the next activity to fetch groups and events
                startActivityForResult(i, 1);
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,"New Group added successfully",Toast.LENGTH_SHORT);
                // We no longer need to add groups to nav menu .

            }

        }

    }
}
