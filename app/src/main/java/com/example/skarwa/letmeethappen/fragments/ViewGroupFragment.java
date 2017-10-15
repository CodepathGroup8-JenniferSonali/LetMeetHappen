package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.utils.Constants;


public class ViewGroupFragment extends DialogFragment {

    TextView tvGroupName;
    ListView lvMembers;
    Button btnCreateEvent;
    String groupName;

    public static ViewGroupFragment newInstance(String name) {

        ViewGroupFragment fragment = new ViewGroupFragment();
        Bundle args = new Bundle();
        args.putString(Constants.GROUP_NAME, name);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        groupName = args.getString(Constants.GROUP_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
        tvGroupName.setText(groupName);

        lvMembers = (ListView) view.findViewById(R.id.lvMembers);

        String[] values = new String[]{"Jennifer",
                "Sonali",
                "Row 3",
                "Row 4",
                "Row 5",
                "Row 6"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        lvMembers.setAdapter(adapter);

        btnCreateEvent = (Button)view.findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //FragmentManager fm = getSupportFragmentManager();
                NewEventFragment eventFragment = NewEventFragment.newInstance(groupName);
                eventFragment.show(getFragmentManager(), "fragment_new_event");
                dismiss();
            }
        });


    }


}
