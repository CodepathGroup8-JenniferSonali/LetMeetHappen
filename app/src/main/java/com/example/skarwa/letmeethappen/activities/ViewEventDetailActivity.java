package com.example.skarwa.letmeethappen.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.fragments.RespondEventInviteFragment;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.skarwa.letmeethappen.R.id.tvDate;
import static com.example.skarwa.letmeethappen.R.id.tvDate2;

/**
 * Created by skarwa on 10/16/17.
 */

public class ViewEventDetailActivity extends AppCompatActivity implements Constants {
    private static final String TAG = "ViewEventDetailActivity";
    Event mEvent;
    String loggedInUserId;
    String loggedInUserDisplayName;
    SharedPreferences sharedPref;
    Map<String,String> attendeeUsers;
    DatabaseReference mDatabase;

    @BindView(R.id.btnRespond)
    Button btnRespond;

    @BindView(R.id.tvHostName)
    TextView tvHostName;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvLocationValue)
    TextView tvLocationVal;

    @BindView(R.id.ivSandClockIcon)
    ImageView respondByImage;

    @BindView(R.id.tvRSVPDate)
    TextView tvRSVPDate;

    @BindView(R.id.tvDates)
    TextView tvDates;

    @BindView(R.id.tvDates2)
    TextView tvDates2;

    @BindView(R.id.etAttendees)
    EditText etAttendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ButterKnife.bind(this);

        sharedPref = this.getSharedPreferences(
                USER_DETAILS, Context.MODE_PRIVATE);

        setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEvent = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_OBJ));
        loggedInUserId = sharedPref.getString(USER_ID, null);
        loggedInUserDisplayName = sharedPref.getString(USER_DISPLAY_NAME, null);

        ButterKnife.bind(this);


        getSupportActionBar().setTitle(mEvent.getEventName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        attendeeUsers = new HashMap<String, String>();
        for(Map.Entry<String,Boolean> entry : mEvent.getAttendedUser().entrySet()){
            if(entry.getValue().equals(true)){
                attendeeUsers.put(entry.getKey(),"");
            }
        }

        mDatabase.child(USERS_ENDPOINT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(attendeeUsers.containsKey(user.getId())){
                        attendeeUsers.put(user.getId(),user.getDisplayName());
                    }
                }
                if(attendeeUsers.values().size() > 0){
                    String attendeeList = attendeeUsers.values().toString();
                    attendeeList = attendeeList.replaceAll("\\[", "").replaceAll("\\]","");
                    etAttendees.setText(attendeeList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to load users.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        generateDetailEventView();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.fade_out, R.animator.fade_in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.actionSave).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return  super.onOptionsItemSelected(item);
    }


    private void generateDetailEventView() {
        tvLocationVal.setText(mEvent.getLocation().getName().toString());
        tvHostName.setText(mEvent.getPlannerName());
        tvDates2.setVisibility(View.INVISIBLE);
        tvRSVPDate.setText(mEvent.getAcceptByDate());

        String dateOptions  = mEvent.getEventDateOptions().keySet().toString();

        if (dateOptions != null && mEvent.getEventFinalDate() == null) {
            tvRSVPDate.setVisibility(View.VISIBLE);
            respondByImage.setVisibility(View.VISIBLE);
            String[] dates = dateOptions.replaceAll("[\\[\\]]", "").split(",");
            tvDates.setText(dates[0].trim());
            if (dates.length>1) {
                tvDates2.setVisibility(View.VISIBLE);
                tvDates2.setText(dates[1].trim());
            }
        } else {
            tvRSVPDate.setVisibility(View.INVISIBLE);
            respondByImage.setVisibility(View.INVISIBLE);
            tvDates.setText(mEvent.getEventFinalDate().toString());
        }

       // if its the planner do not show respond button.
        if(mEvent.getPlannerId().equals(loggedInUserId) || mEvent.getEventStatus().equals(EventStatus.SUCCESSFUL.name()) || mEvent.getEventStatus().equals(EventStatus.CONFIRMED.name())){
           btnRespond.setVisibility(View.INVISIBLE);
        } else {
            btnRespond.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    RespondEventInviteFragment respondFragment = RespondEventInviteFragment.newInstance(Parcels.wrap(mEvent),loggedInUserId);
                    respondFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                    respondFragment.show(getSupportFragmentManager(), "fragment_respond");

                    Log.d("DEBUG", "display Respond dialog here");
                }
            });
        }
    }
}
