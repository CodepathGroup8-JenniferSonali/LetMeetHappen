package com.example.skarwa.letmeethappen.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.Location;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.models.User;

import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.DateUtils;
import com.example.skarwa.letmeethappen.utils.FCM;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.example.skarwa.letmeethappen.R.id.npMinYes;
import static com.example.skarwa.letmeethappen.utils.Constants.GROUP_OBJ;


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class NewEventFragment extends DialogFragment implements SelectDatesFragment.OnDatePass {

    private static final String TAG = "NewEventFragment";
    final static String ISRANGE = "IS_RANGE";
    final static String DATE_PICKER = "datePicker";
    final static int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.etEventName)
    EditText etEventName;

    @BindView(R.id.etDates)
    EditText etDates;

    @BindView(R.id.etRSVPDate)
    EditText etRSVPDate;

    @BindView(R.id.etLocationValue)
    EditText etLocationValue;

    @BindView(R.id.npMinYes)
    NumberPicker numberPicker;

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
        View view = inflater.inflate(R.layout.fragment_new_event, container);
        getDialog().setTitle(group.getName());
        event = new Event();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //get count of group members
        int count = group.getMembers().keySet().size();
        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(count);
        numberPicker.setWrapSelectorWheel(true);


        //Set a value change listener for NumberPicker
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){

                int getMin = newVal;
                if (getMin != 0) {
                    event.setMinAcceptance(getMin);
                } else {
                    event.setMinAcceptance(2);   //default to 2 for now
                }
            }
        });

        /*List<String> list = new ArrayList<String>();
        for (int i = 2; i <= count; i++) {
            list.add(String.valueOf(i));
        }ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sbMinYes.setAdapter(dataAdapter);*/

       etLocationValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = intentBuilder.build(getActivity());
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getActivity(), "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }

            }
        });




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


        Button btn = (Button) view.findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                event.setEventName(etEventName.getText().toString());
                event.setAcceptByDate(etRSVPDate.getText().toString());
                event.setEventStatus(EventStatus.PENDING.name());
                event.setGroup(group);




                listener = (OnCreateEventClickListener) getActivity();
                listener.onCreateEvent(event);

                Log.d(TAG, "Send Button Clicked");
                dismiss();

                sendInvite(group);
            }
        });

    }


    public void sendInvite(Group group) {
        // send invites to all group members to join
        //notify group members of the new invite

        ArrayList<String> tokens = new ArrayList<>();
        Map<String, String> tMap = group.getTokenSet();
        if (tMap != null) {
            for (String id : tMap.keySet()) {
                // exclude the host
                //if (!id.equals(loggedInUserId))
                {
                    tokens.add(tMap.get(id));
                    PackageManager pm = getActivity().getPackageManager();
                    String email = User.decode(id);
                    //sendEmail( User.decode(id), "Jennifer", id );
                    //sendEmail( "pizarro.concham@gmail.com", "Jennifer", id ); // for demo

                    break;
                }
            }
            try {
                //Log.d(TAG, "TOKEN id = " + loggedInUserId);
                if (tokens.size() > 0) {
                    new FCM().execute(tokens.toArray());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(),data);
                LatLng latlng =  place.getLatLng();

                Location selectedLocation = new Location();
                selectedLocation.setName(place.getName().toString());

                selectedLocation.setLatitude(latlng.latitude);
                selectedLocation.setLongitude(latlng.longitude);

                event.setLocation(selectedLocation);
                etLocationValue.setText(place.getName().toString());

                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


         void sendEmail( String sEmail, String fromName, String toName) {
            String msg = String.format("Hi %s,\n\n %s has invited you.  Please install \"Let Meet Happen\" app", toName, fromName);
            String uriText =
                    "mailto:"+sEmail +
                            "?subject=" + Uri.encode("LET MEET HAPPEN :  You've been invited!") +
                            "&body=" + Uri.encode(msg);

            Uri uri = Uri.parse(uriText);

            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(uri);
            if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(Intent.createChooser(sendIntent, "Send email"));
            }
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


