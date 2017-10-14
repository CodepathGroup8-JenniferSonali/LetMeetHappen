package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;

import org.json.JSONException;


public class ViewEventFragment extends DialogFragment {

    TextView tvEventName;
    Button btnRespond;
    Event mEvent;

    public static ViewEventFragment newInstance(Event event) {

        ViewEventFragment fragment = new ViewEventFragment();
        Bundle args = new Bundle();
        //args.putParcelable("event", Parcels.wrap(event));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        //mEvent = (Event) Parcels.unwrap(args.getParcelable("event"));

        //TODO  below is for testing only
        try {
            mEvent = Event.fromJSON(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvEventName = (TextView) view.findViewById(R.id.tvEventName);
        tvEventName.setText(mEvent.getEventName());


        btnRespond = (Button)view.findViewById(R.id.btnRespond);
        btnRespond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //TODO display Respond dialog
                /*
                //FragmentManager fm = getSupportFragmentManager();
                NewEventFragment eventFragment = NewEventFragment.newInstance(groupName);
                eventFragment.show(getFragmentManager(), "fragment_new_event");
                dismiss();
                */
                Log.d("DEBUG", "display Respond dialog here");
            }
        });


    }


}
