package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.skarwa.letmeethappen.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class NewEventFragment extends DialogFragment implements SelectDatesFragment.OnDatePass{

    final static String ISRANGE = "IS_RANGE";

    EditText etEventName;
    EditText etDates;
    EditText etRSVPDate;
    String mTitle; //group name


    public NewEventFragment() {
    }

    public static NewEventFragment newInstance(String title) {

        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mTitle = args.getString("title");
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setTitle(mTitle);

        etEventName = (EditText) view.findViewById(R.id.etEventName);

        // Show soft keyboard automatically and request focus to field
        etEventName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etDates = (EditText)view.findViewById(R.id.etDates);

        etDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectDatesFragment newFragment = new SelectDatesFragment();

                newFragment.setTargetFragment(NewEventFragment.this, 300);
                Bundle bundle = new Bundle();
                bundle.putBoolean(ISRANGE, true);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        etRSVPDate = (EditText)view.findViewById(R.id.etRSVPDate);
        etRSVPDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectDatesFragment newFragment = new SelectDatesFragment();

                newFragment.setTargetFragment(NewEventFragment.this, 300);
                Bundle bundle = new Bundle();
                bundle.putBoolean(ISRANGE, false);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });


        Button btn = (Button)view.findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d("DEBUG", "button clicked in new event fragment");
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


