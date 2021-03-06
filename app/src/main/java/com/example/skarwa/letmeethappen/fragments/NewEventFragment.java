package com.example.skarwa.letmeethappen.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;
import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.models.Location;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.DBUtils;
import com.example.skarwa.letmeethappen.utils.DateUtils;
import com.example.skarwa.letmeethappen.utils.FCM;
import com.example.skarwa.letmeethappen.utils.Validation;
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
import static com.example.skarwa.letmeethappen.R.string.send;
import static com.example.skarwa.letmeethappen.utils.Constants.GROUP_OBJ;


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class NewEventFragment extends DialogFragment implements SelectDatesFragment.OnDatePass {

    private static final String TAG = "NewEventFragment";
    final static String ISRANGE = "IS_RANGE";
    final static String DATE_PICKER = "datePicker";
    final static int PLACE_PICKER_REQUEST = 1;
    boolean isValidLocation = false;

    @BindView(R.id.input_layout_dates)
    TextInputLayout inputLayoutDates;

    @BindView(R.id.input_layout_eventName)
    TextInputLayout inputLayoutEventName;

    @BindView(R.id.input_layout_locationValue)
    TextInputLayout inputLayoutLocation;

    @BindView(R.id.input_layout_rsvpdate)
    TextInputLayout inputLayoutRSVPDate;

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

    @BindView(R.id.btn_invite)
    Button btnInvite;

    @BindView(R.id.etMessage)
    EditText etMessage;

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

        //getDialog().setTitle(group.getName());
        event = new Event();
        ButterKnife.bind(this, view);

        etDates.addTextChangedListener(new MyTextWatcher(etDates));
        etEventName.addTextChangedListener(new MyTextWatcher(etEventName));
        etLocationValue.addTextChangedListener(new MyTextWatcher(etLocationValue));
        etRSVPDate.addTextChangedListener(new MyTextWatcher(etRSVPDate));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(group.getName());

        //get count of group members
        int count = group.getMembers().keySet().size();
        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(count);
        numberPicker.setWrapSelectorWheel(true);

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


        btnInvite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if ( checkValidation () ){
                    event.setEventName(etEventName.getText().toString());
                    event.setAcceptByDate(etRSVPDate.getText().toString());
                    event.setEventStatus(EventStatus.PENDING.name());
                    event.setGroup(group);

                    int valueSelected = numberPicker.getValue();
                    event.setMinAcceptance(valueSelected);

                    String message = etMessage.getText().toString();
                    if(message != null){
                        event.setPlannerMsgToGroup(message);
                    }

                    listener = (OnCreateEventClickListener) getActivity();
                    listener.onCreateEvent(event);

                    Log.d(TAG, "Send Button Clicked");
                    dismiss();

                    sendInvite(group, etMessage.getText().toString());
                } else {
                    Toast.makeText(getActivity(),"Please fill required fields",Toast.LENGTH_SHORT);
                }
            }
        });
    }


    public void sendInvite(Group group, String msg) {
        // send invites to all group members to join
        //notify group members of the new invite

        String bodyMsg = "\n\""+msg+"\" - "+event.getPlannerName();

        ArrayList<String> tokens = new ArrayList<>();
        Map<String, String> tMap = group.getTokenSet();

        for (String id : group.getMembers().keySet()) {
            if (id.equals(event.getPlannerId())) {
                continue;
            }
            String tId = null;
            if (tMap != null && tMap.containsKey(id)) {
                tId = tMap.get(id);
            } else {
                // check our DB
                if (DBUtils.dbTokens != null) {
                    tId = DBUtils.dbTokens.get(id);
                }
            }

            if (tId != null) {
                tokens.add(tId);

                boolean sendEmail = false;
                if (sendEmail) {
                    sendEmail( User.decode(id), "", id );
                }

            }
        }

        try {
            //Log.d(TAG, "TOKEN id = " + loggedInUserId);
            if (tokens.size() > 0) {
                for (String t : tokens) {
                    String[] tArr = new String[2];
                    tArr[0] = t;
                    tArr[1] = bodyMsg;
                    new FCM().execute(tArr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

                isValidLocation = true;
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
            if (isRange) {
                event.addEventDateOptions(DateUtils.formatDateToString(date));
            }
            str += new SimpleDateFormat(Constants.DATE_PATTERN).format(date.getTime()) +"\n";
        }
        EditText et = (isRange) ? etDates : etRSVPDate;
        et.setText(str);
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!validateName()) ret = false;
        if (!validateLocation()) ret = false;
        if (!validateDates()) ret = false;
        if (!validateRSVPDate()) ret = false;
        return ret;
    }

    private boolean validateName() {
        if (etEventName.getText().toString().trim().isEmpty()) {
            inputLayoutEventName.setError(getString(R.string.err_msg_event_name));
            requestFocus(inputLayoutEventName);
            return false;
        } else {
            inputLayoutEventName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLocation() {
        if (etLocationValue.getText().toString().trim().isEmpty() || !isValidLocation) {
            inputLayoutLocation.setError(getString(R.string.err_msg_location));
            requestFocus(inputLayoutLocation);
            return false;
        } else {
            inputLayoutLocation.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDates() {
        if (etDates.getText().toString().trim().isEmpty()) {
            inputLayoutDates.setError(getString(R.string.err_msg_date));
            requestFocus(inputLayoutDates);
            return false;
        } else {
            inputLayoutDates.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateRSVPDate() {
        if (etRSVPDate.getText().toString().trim().isEmpty()) {
            inputLayoutRSVPDate.setError(getString(R.string.err_msg_rsvp_date));
            requestFocus(inputLayoutRSVPDate);
            return false;
        } else {
            inputLayoutRSVPDate.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
           getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etEventName:
                    validateName();
                    break;
                case R.id.etRSVPDate:
                   validateRSVPDate();
                    break;
                case R.id.etLocationValue:
                    validateLocation();
                    break;
                case R.id.etDates:
                    validateDates();
                    break;
            }
        }
    }
}


