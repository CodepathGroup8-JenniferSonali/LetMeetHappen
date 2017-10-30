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
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.fragments.RespondEventInviteFragment;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by skarwa on 10/16/17.
 */

public class ViewEventDetailActivity extends AppCompatActivity implements Constants {
    private static final String TAG = "ViewEventDetailActivity";
    Event mEvent;
    String loggedInUserId;
    String loggedInUserDisplayName;
    SharedPreferences sharedPref;

    @BindView(R.id.btnRespond)
    Button btnRespond;

    @BindView(R.id.tvHostName)
    TextView tvHostName;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvLocationValue)
    TextView tvLocationVal;

    @BindView(R.id.tvRSVPDate)
    TextView tvRSVPDate;

    @BindView(R.id.tvDates)
    TextView tvDates;

    @BindView(R.id.tvDates2)
    TextView tvDates2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ButterKnife.bind(this);

        sharedPref = this.getSharedPreferences(
                USER_DETAILS, Context.MODE_PRIVATE);

        setSupportActionBar(toolbar);

        mEvent = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_OBJ));
        loggedInUserId = sharedPref.getString(USER_ID, null);
        loggedInUserDisplayName = sharedPref.getString(USER_DISPLAY_NAME, null);

        ButterKnife.bind(this);

        generateDetailEventView();
        getSupportActionBar().setTitle(mEvent.getEventName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        tvRSVPDate.setText(mEvent.getAcceptByDate());
        tvHostName.setText(mEvent.getPlannerName());

        Iterator<String> itr = mEvent.getEventDateOptions().keySet().iterator();
        tvDates.setText(itr.next());

        if (itr.hasNext()) {
            tvDates2.setText(itr.next());
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
