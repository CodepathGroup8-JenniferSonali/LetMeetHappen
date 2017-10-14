package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class RespondFragment extends DialogFragment implements SelectDatesFragment.OnDatePass{

    final static String ISRANGE = "IS_RANGE";

    EditText etEventName;
    EditText etDates;
    EditText etRSVPDate;
    String mEventName;
    ListView lvDates;
    TextView tvMessage;


    public RespondFragment() {
    }

    public static RespondFragment newInstance(String eventName) {

        RespondFragment fragment = new RespondFragment();
        Bundle args = new Bundle();
        args.putString("event_name", eventName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mEventName = args.getString("event_name");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_respond, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setTitle(mEventName);

        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        //tvMessage.setText(event.getMessage);

        lvDates = (ListView) view.findViewById(R.id.lvDates);

        Button btnSend = (Button)view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("DEBUG", "button clicked in new respond fragment");
                dismiss();
            }
        });

    }


    @Override
    public void onDatePass(boolean isRange, List<Date> dates) {
        String str = "";

        for (Date date : dates) {
            str += new SimpleDateFormat("E   yyyy.MM.dd").format(date.getTime()) +"\n";
        }

        EditText et = (isRange) ? etDates : etRSVPDate;
        et.setText(str);
    }
}


