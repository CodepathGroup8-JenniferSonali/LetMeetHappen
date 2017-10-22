package com.example.skarwa.letmeethappen.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.fragments.RespondEventInviteFragment;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mEvent = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_OBJ));
        loggedInUserId = getIntent().getStringExtra(USER_ID);
        loggedInUserDisplayName = getIntent().getStringExtra(USER_DISPLAY_NAME);


        ButterKnife.bind(this);

        generateDetailEventView();
        getSupportActionBar().setTitle(mEvent.getEventName());

        //TODO temporary fix...when we go back the user object becomes null. so not showing back icon for now.
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void generateDetailEventView() {
        tvLocationVal.setText(mEvent.getLocation().getUserFriendlyName().toString());
        tvRSVPDate.setText(mEvent.getAcceptByDate());
        tvHostName.setText(loggedInUserDisplayName);
        String dates =  mEvent.getEventDateOptions().keySet().toString();

        tvDates.setText(dates);

       // if its the planner do not show respond button.
        if(mEvent.getPlannerId().equals(loggedInUserId)){
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
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
