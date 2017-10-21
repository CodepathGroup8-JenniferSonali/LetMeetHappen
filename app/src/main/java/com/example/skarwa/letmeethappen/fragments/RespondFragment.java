package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.media.VolumeProviderCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.skarwa.letmeethappen.utils.Constants.EVENTS_ENDPOINT;
import static com.example.skarwa.letmeethappen.utils.Constants.EVENT_OBJ;
import static com.example.skarwa.letmeethappen.utils.Constants.GROUPS_ENDPOINT;
import static com.example.skarwa.letmeethappen.utils.Constants.GROUP_OBJ;
import static com.example.skarwa.letmeethappen.utils.Constants.USER_EVENTS;


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class RespondFragment extends DialogFragment implements SelectDatesFragment.OnDatePass{

    private static final String TAG = "RespondFragment";
    final static String ISRANGE = "IS_RANGE";

    EditText etEventName;
    EditText etDates;
    EditText etRSVPDate;
    String mEventName;

    @BindView(R.id.lvDates)
    ListView lvDates;

    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @BindView(R.id.btnSend)
    Button btnSend;

    Event event;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]


    public RespondFragment() {
    }

    public static RespondFragment newInstance(Parcelable event) {

        RespondFragment fragment = new RespondFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EVENT_NAME, event);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        event = Parcels.unwrap(args.getBundle(EVENT_OBJ));
        mEventName = args.getString(Constants.EVENT_NAME);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_respond, container);

        ButterKnife.bind(this,view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setTitle(mEventName);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("RespondFragment", "Send Button clicked");

                //if response was yes
                //do this
                //1) update the user as one of the attendies
                //2) did we reach minAcceptance ? if yes set status as confirmed , else do nothing
                //3)
                //else
                //do this
                updateEventStatus();



                dismiss();
            }
        });
    }

    private void updateEventStatus() {
        //TODO update event with response
        event.setEventStatus(EventStatus.CONFIRMED.name());
        Map<String,Boolean> groupMembers =  event.getGroup().getMembers();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+EVENTS_ENDPOINT+"/" + event.getId(), event);



        //TODO update event for all the users part of the event

        for (String userId : groupMembers.keySet()){
            childUpdates.put("/"+USER_EVENTS+"/"+userId+"/"+event.getId(),event);
        }

        //DatabaseReference groupMembers = mDatabase.child(GROUPS_ENDPOINT).child(event.getGroup().getId()).child("members");
    }


    @Override
    public void onDatePass(boolean isRange, List<Date> dates) {
        String str = "";

        for (Date date : dates) {
            str += new SimpleDateFormat(Constants.DATE_PATTERN).format(date.getTime()) +"\n";
        }
        EditText et = (isRange) ? etDates : etRSVPDate;
        et.setText(str);
    }
}


