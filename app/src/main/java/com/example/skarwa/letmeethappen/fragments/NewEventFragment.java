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


/**
 * Created by jennifergodinez on 9/27/17.
 */

public class NewEventFragment extends DialogFragment {



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        EditText etEventName = (EditText) view.findViewById(R.id.etEventName);

        // Show soft keyboard automatically and request focus to field
        etEventName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        Button btn = (Button)view.findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d("DEBUG", "button clicked in new event fragment");
                dismiss();
            }
        });

    }
}


