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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.activities.ViewEventsActivity;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.services.MyEventTrackingService;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.skarwa.letmeethappen.utils.Constants.EVENTS_ENDPOINT;
import static com.example.skarwa.letmeethappen.utils.Constants.EVENT_OBJ;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_EVENTS;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_ID;


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class RespondEventInviteFragment extends DialogFragment {

    private static final String TAG = "RespEventInviteFrag";

    Event event;
    String loggedInUserId;

    @BindView(R.id.ivHostIcon)
    ImageView ivHostIcon;

    @BindView(R.id.tvMsg)
    TextView tvMsg;

    @BindView(R.id.btnSendUpdate)
    Button btnSendUpdate;

    @BindView(R.id.swResponse)
    Switch response;

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

        getDialog().setTitle(event.getEventName());

        Glide.with(getContext())
                .load(event.getHostProfileImage())
                .placeholder(R.drawable.ic_host_icon)
                .bitmapTransform(new RoundedCornersTransformation(getContext(), 15, 10))
                .into(ivHostIcon);

        String[] dateArray = event.getEventDateOptions().keySet().toArray(new String[2]);
        rbDate1.setText(dateArray[0]);
        rbDate1.setEnabled(false);
        rbDate2.setText(dateArray[1]);
        rbDate2.setEnabled(false);

        if (event.getPlannerMsgToGroup() != null) {
            tvMsg.setText(event.getPlannerMsgToGroup());
        } else {
            tvMsg.setText(Constants.DEFAULT_MSG);
        }
        //TODO enable yes , no ,send update only when date selected
        //TODO set date on event when checked

        response.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Switch selected");

                if(response.isChecked()){
                    event.addAttendedUser(loggedInUserId, response.isChecked());
                    if (event.getEventDateOptions().size() > 1) {
                        rbDate1.setEnabled(true);
                        rbDate2.setEnabled(true);
                    } else {
                        rbDate1.setChecked(true);
                    }
                } else {
                    rbDate1.setEnabled(false);
                    rbDate2.setEnabled(false);
                }
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
        int attendingCount = 0;
        if(rbDate1.isChecked()){
            int value = event.getEventDateOptions().get(rbDate1.getText());
            event.getEventDateOptions().put(rbDate1.getText().toString(),++value);
        } else if(rbDate2.isChecked()){
            int value = event.getEventDateOptions().get(rbDate2.getText());
            event.getEventDateOptions().put(rbDate2.getText().toString(),++value);
        }

        //loop a Map
        for (Map.Entry<String, Boolean> entry : event.getAttendedUser().entrySet()) {
            if (entry.getValue().equals(Boolean.TRUE)) {
                attendingCount++;
            }
            if (attendingCount >= event.getMinAcceptance()) {
                event.setEventStatus(EventStatus.CONFIRMED.name());
                MyEventTrackingService.notifyGroup(event.getGroup(), event.getEventName()+" is confirmed.");
                String[] dates = event.getEventDateOptions().keySet().toArray(new String[2]);
                Integer[] count = event.getEventDateOptions().values().toArray(new Integer[2]);

                if (event.getEventDateOptions().size() > 1) {
                    if (count[0] > count[1]) {
                        event.setEventFinalDate(dates[0]);
                    } else if (count[0] < count[1]) {
                        event.setEventFinalDate(dates[1]);
                    } else { //equal count
                        event.setEventFinalDate(dates[0]); //defaults to first option
                    }
                } else {
                    event.setEventFinalDate(dates[0]);
                }
            }
        }




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

    }
}


