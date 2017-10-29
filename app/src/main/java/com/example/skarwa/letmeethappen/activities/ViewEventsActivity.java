package com.example.skarwa.letmeethappen.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.adapters.EventsPagerAdapter;
import com.example.skarwa.letmeethappen.fragments.EventsListFragment;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.services.MyAlarmReceiver;
import com.example.skarwa.letmeethappen.services.MyEventTrackingService;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jennifergodinez on 10/9/17.
 */

public class ViewEventsActivity extends AppCompatActivity implements
        Constants,
        EventsListFragment.OnEventClickListener {

    private static final String TAG = "ViewEventsActivity";
    EventsPagerAdapter pagerAdapter;
    ActionBarDrawerToggle drawerToggle;
    User loggedInUser;
    ArrayList<? extends Parcelable> friends;
    int tabIndex;
    SharedPreferences sharedPref;


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

    static class NavHeaderLayout{
        @BindView(R.id.tvEmail)
        TextView tvEmail;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        ButterKnife.bind(this);

        //get the NavHeader
        View headerLayout = nvDrawer.getHeaderView(0);

        NavHeaderLayout navHeaderLayout = new NavHeaderLayout();
        ButterKnife.bind(navHeaderLayout,headerLayout);

        loggedInUser = Parcels.unwrap(getIntent().getParcelableExtra(Constants.USER_OBJ));
        friends = (ArrayList<? extends Parcelable>) getIntent().getParcelableArrayListExtra(Constants.FRIENDS_OBJ);
        tabIndex = getIntent().getIntExtra(Constants.SHOW_TAB_INDEX,0);

        sharedPref = this.getSharedPreferences(
                USER_DETAILS, Context.MODE_PRIVATE);

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
        nvDrawer.setItemIconTintList(null);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        Glide.with(this).load(sharedPref.getString(USER_PROFILE_URL,null))
                .into(navHeaderLayout.ivProfilePic);
        navHeaderLayout.tvName.setText(sharedPref.getString(USER_DISPLAY_NAME,null));
        navHeaderLayout.tvEmail.setText(sharedPref.getString(USER_EMAIL,null));

        pagerAdapter = new EventsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(tabIndex);
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

                            case R.id.myGroups:
                                Intent i1 = new Intent(getBaseContext(), MyGroupsListActivity.class);
                                i1.putParcelableArrayListExtra(Constants.FRIENDS_OBJ, (ArrayList<? extends Parcelable>) friends);
                                startActivity(i1);
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
        menu.findItem(R.id.actionSave).setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                if (drawerToggle.onOptionsItemSelected(item)) {
                    return true;
                }

            case R.id.miNotify:
                //TODO : inform user of the ne notification received
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

    public void onEventClick(Event event) {
        Toast.makeText(this,"Show Event Details",Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(), ViewEventDetailActivity.class);
        i.putExtra(Constants.EVENT_OBJ, Parcels.wrap(event));
        startActivity(i);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    // Define the callback for what to do when data is received
    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                viewPager.getAdapter().notifyDataSetChanged();
                //String resultValue = intent.getStringExtra("resultValue");
                //Toast.makeText(ViewEventsActivity.this, resultValue, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(MyEventTrackingService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);
        // or `registerReceiver(testReceiver, filter)` for a normal broadcast
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(testReceiver);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }



    // Call `launchTestService()` in the activity
    // to startup the service
   /* public void launchEventTrackingService() {
        // Construct our Intent specifying the Service
        Intent i = new Intent(this, MyEventTrackingService.class);
        // Add extras to the bundle
        i.putExtra(USER_ID,sharedPref.getString(USER_ID,getUid()));  //TODO : update as needed
        // Start the service
        startService(i);
    }*/



    //cancel alarm
    /*public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }*/
}
