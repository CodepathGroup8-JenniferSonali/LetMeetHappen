package com.example.skarwa.letmeethappen.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.adapters.EventAdapter;
import com.example.skarwa.letmeethappen.adapters.EventsPagerAdapter;
import com.example.skarwa.letmeethappen.fragments.EventsListFragment;
import com.example.skarwa.letmeethappen.fragments.NewEventFragment;
import com.example.skarwa.letmeethappen.fragments.ViewGroupFragment;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.MultiSpinner;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.skarwa.letmeethappen.R.string.groups;

/**
 * Created by jennifergodinez on 10/9/17.
 */

public class ViewEventsActivity extends AppCompatActivity
        implements NewEventFragment.OnCreateEventClickListener,Constants,
        EventsListFragment.OnEventClickListener ,ViewGroupFragment.SendInviteListener{

    private static final String TAG = "ViewEventsActivity";
    EventsPagerAdapter pagerAdapter;
    ActionBarDrawerToggle drawerToggle;
    User loggedInUser;
    ArrayList<? extends Parcelable> friends;


    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nvView)
    NavigationView nvDrawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        ButterKnife.bind(this);

        loggedInUser = Parcels.unwrap(getIntent().getParcelableExtra(Constants.USER_OBJ));
        friends = (ArrayList<? extends Parcelable>) getIntent().getParcelableArrayListExtra(Constants.FRIENDS_OBJ);


        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // Find our drawer view
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        pagerAdapter = new EventsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        DialogFragment fragment = null;
                        Class fragmentClass = null;
                        FragmentManager fm = getSupportFragmentManager();


                        switch(menuItem.getItemId()) {

                            case R.id.addGroup:
                                Intent i = new Intent(getBaseContext(), NewGroup_Activity.class);
                                i.putParcelableArrayListExtra(Constants.FRIENDS_OBJ, (ArrayList<? extends Parcelable>) friends);
                                //send user details to the next activity to fetch groups and events
                                startActivity(i);


                                //NewGroupFragment groupFragment = NewGroupFragment.newInstance(friends);
                                //groupFragment.show(fm, "fragment_new_group");

                                break;

                            case R.id.group1:  //TODO, we need to dynamically get the ID
                              //  ViewGroupFragment viewgroupFragment = ViewGroupFragment.newInstance("Close Friends");
                               // viewgroupFragment.show(fm, "fragment_view_group");

                                break;

                            //case

                                //TODO else if settings selects
                                //launch settings page
                                //launchSettingsDialog/ Activity

                            default:
                                break;


                        }


                        // Highlight the selected item has been done by NavigationView
                        menuItem.setChecked(true);
                        // Set action bar title
                        //setTitle(menuItem.getTitle());
                        // Close the navigation drawer
                        mDrawer.closeDrawers();
                        return true;
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                if (drawerToggle.onOptionsItemSelected(item)) {
                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onCreateEvent(Event event) {

        event.setPlanner(loggedInUser);
        event.addAttendedUser(loggedInUser.getId(),true);
        Toast.makeText(this, "Saving Event...", Toast.LENGTH_SHORT).show();

       // String key = mDatabase.child(EVENTS_ENDPOINT).child(loggedInUser.)

        String key = mDatabase.child(EVENTS_ENDPOINT).push().getKey();
        Map<String, Object> eventValues = event.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+EVENTS_ENDPOINT+"/" + key, event);
        childUpdates.put("/"+USER_EVENTS+"/" + loggedInUser.getId() + "/" + key, eventValues);
        //TODO add this to sending invites as well.

        mDatabase.updateChildren(childUpdates);
    }

    public void onItemsSelected(boolean[] selected) {

    }

    public void onEventClick(Event event) {
        Toast.makeText(this,"Show Event Details",Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(), ViewEventDetailActivity.class);
        i.putExtra(Constants.EVENT_OBJ, Parcels.wrap(event));
        startActivity(i);
    }


    public void onSendInvite(Group group) {
        //save group in DB
        mDatabase.child(GROUP_ENDPOINT).child(group.getName()).setValue(group);

        //Add group  to NavDrawer


        //open Nav Drawer to show newly added group
        mDrawer.openDrawer(nvDrawer);
    }


    /**
     * This will get all the groups belonging to authorized user.
     */
    //TODO :test this works
    public void populateAllUserGroups(){
        // List the names of all User's groups
        // fetch a list of User's groups
        mDatabase.child("users/"+loggedInUser.getId()+"/groups").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                // for each group, fetch the name and print it
                String groupKey = snapshot.getKey();
                Log.d(TAG,"onChildAdded:"+snapshot.getKey());

                //TODO get all groups and attach it to navigation drawer
                mDatabase.child("groups/" + groupKey + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d(TAG,loggedInUser + " is a member of this group: " + snapshot.getValue());
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        // ignore
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
