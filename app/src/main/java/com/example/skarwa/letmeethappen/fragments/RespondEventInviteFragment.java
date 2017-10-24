package com.example.skarwa.letmeethappen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.activities.ViewEventsActivity;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.skarwa.letmeethappen.utils.Constants.EVENTS_ENDPOINT;
import static com.example.skarwa.letmeethappen.utils.Constants.EVENT_OBJ;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_EVENTS;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_ID;


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class RespondEventInviteFragment extends DialogFragment {

    private static final String TAG = "RespEventInviteFrag";

    String mEventName;
    Event event;
    String loggedInUserId;

    @BindView(R.id.tvRSVPDate)
    TextView tvRSVPDate;

    @BindView(R.id.etMessage)
    EditText etMessage;

    @BindView(R.id.btnSendUpdate)
    Button btnSendUpdate;

    @BindView(R.id.tvLocationValue)
    TextView tvLocation;

    @BindView(R.id.btnYes)
    Button btnYes;

    @BindView(R.id.btnNo)
    Button btnNo;

    @BindView(R.id.tvHostName)
    TextView tvHostName;

    @BindView(R.id.rbDateOption1)
    RadioButton rbDate1;

    @BindView(R.id.rbDateOption2)
    RadioButton rbDate2;


    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]


    public RespondEventInviteFragment() {
    }

    public static RespondEventInviteFragment newInstance(Parcelable event, String userId) {

        RespondEventInviteFragment fragment = new RespondEventInviteFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EVENT_OBJ, event);
        args.putString(USER_ID, userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        event = Parcels.unwrap(args.getParcelable(EVENT_OBJ));
        loggedInUserId = args.getString(USER_ID);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_respond_event_invite, container);

        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setTitle(event.getEventName());

        tvLocation.setText(event.getLocation().getName().toString());
        tvRSVPDate.setText(event.getAcceptByDate());
        tvHostName.setText(event.getPlannerName());

        String[] dateArray = event.getEventDateOptions().keySet().toArray(new String[2]);
        rbDate1.setText(dateArray[0]);
        rbDate2.setText(dateArray[1]);

        if (event.getPlannerMsgToGroup() != null) {
            etMessage.setText(event.getPlannerMsgToGroup());
        } else {
            etMessage.setText(Constants.DEFAULT_MSG);
        }
        //TODO enable yes , no ,send update only when date selected
        //TODO set date on event when checked

        btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Yes Button clicked");
                updateEvent(loggedInUserId, true);
            }
        });


        btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "No Button clicked");
                updateEvent(loggedInUserId, false);
            }
        });

        btnSendUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "Send Update clicked");
                sendUpdate();
            }
        });
    }


    private void sendUpdate() {
        //TODO update event with response
        Map<String, Boolean> groupMembers = event.getGroup().getMembers();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + EVENTS_ENDPOINT + "/" + event.getId(), event);

        //TODO update event for all the users part of the event
        for (String userId : groupMembers.keySet()) {
            childUpdates.put("/" + USER_EVENTS + "/" + userId + "/" + event.getId(), event);
        }

        mDatabase.updateChildren(childUpdates);

        Intent intent = new Intent(getActivity(), ViewEventsActivity.class);
        startActivity(intent);

        //DatabaseReference groupMembers = mDatabase.child(GROUPS_ENDPOINT).child(event.getGroup().getId()).child("members");
    }

    private void updateEvent(String userId, boolean response) {
        int attendingCount = 0;
        event.addAttendedUser(userId, true);

        //loop a Map
        for (Map.Entry<String, Boolean> entry : event.getAttendedUser().entrySet()) {
            if (entry.getValue().equals(Boolean.TRUE)) {
                attendingCount++;
            }
            if (attendingCount >= event.getMinAcceptance()) {
                event.setEventStatus(EventStatus.CONFIRMED.name());
            }
        }
    }
}


