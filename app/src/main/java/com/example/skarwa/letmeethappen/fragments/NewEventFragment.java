package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.models.Location;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.DateUtils;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.skarwa.letmeethappen.utils.Constants.GROUP_OBJ;


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class NewEventFragment extends DialogFragment implements SelectDatesFragment.OnDatePass{

    private static final String TAG = "NewEventFragment";
    final static String ISRANGE = "IS_RANGE";
    final static String DATE_PICKER = "datePicker";

    @BindView(R.id.etEventName)
    EditText etEventName;

    @BindView(R.id.etDates)
    EditText etDates;

    @BindView(R.id.etRSVPDate)
    EditText etRSVPDate;

    @BindView(R.id.etLocation)
    EditText etLocation;

    Event event;
    Group group;

    // listener will the activity instance containing fragment
    private OnCreateEventClickListener listener;


    // Define the events that the fragment will use to communicate
    public interface OnCreateEventClickListener {
        // This can be any number of events to be sent to the activity
        public void onCreateEvent(Event event);
    }
    public NewEventFragment() {
    }

    public static NewEventFragment newInstance(Parcelable group) {
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        args.putParcelable(GROUP_OBJ, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        group = Parcels.unwrap(args.getParcelable(GROUP_OBJ));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_new_event, container);
        getDialog().setTitle(group.getName());
        event = new Event();
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        // Show soft keyboard automatically and request focus to field
        etEventName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectDatesFragment newFragment = new SelectDatesFragment();

                newFragment.setTargetFragment(NewEventFragment.this, 300);
                Bundle bundle = new Bundle();
                bundle.putBoolean(ISRANGE, true);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), DATE_PICKER);
            }
        });

        etRSVPDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectDatesFragment newFragment = new SelectDatesFragment();

                newFragment.setTargetFragment(NewEventFragment.this, 300);
                Bundle bundle = new Bundle();
                bundle.putBoolean(ISRANGE, false);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), DATE_PICKER);
            }
        });


        Button btn = (Button)view.findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Location location = new Location();
                location.setUserFriendlyName(etLocation.getText().toString());

                event.setEventName(etEventName.getText().toString());
                event.setAcceptByDate(etRSVPDate.getText().toString());
                event.setEventStatus(EventStatus.PENDING.name());
                event.setGroup(group);

                event.setLocation(location);
                event.setMinAcceptance(2); //default to 2 for now

                listener = (OnCreateEventClickListener)getActivity();
                listener.onCreateEvent(event);

                Log.d(TAG, "Send Button Clicked");
                dismiss();
            }
        });

    }

    @Override
    public void onDatePass(boolean isRange, List<Date> dates) {
        String str = "";

        for (Date date : dates) {
            event.addEventDateOptions(DateUtils.formatDateToString(date));
            str += new SimpleDateFormat(Constants.DATE_PATTERN).format(date.getTime()) +"\n";
        }
        EditText et = (isRange) ? etDates : etRSVPDate;
        et.setText(str);
    }
}


