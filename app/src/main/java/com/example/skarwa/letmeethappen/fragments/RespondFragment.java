package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
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
import com.example.skarwa.letmeethappen.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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


    public RespondFragment() {
    }

    public static RespondFragment newInstance(String eventName) {

        RespondFragment fragment = new RespondFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EVENT_NAME, eventName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mEventName = args.getString(Constants.EVENT_NAME);
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
        getDialog().getWindow().setTitle(mEventName);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("RespondFragment", "Send Button clicked");
                dismiss();
            }
        });
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


