package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.utils.Constants;

import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.models.UserGroupStatus;
import com.example.skarwa.letmeethappen.utils.DateUtils;

import org.parceler.Parcels;

import java.util.Date;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.skarwa.letmeethappen.R.string.addNewGroup;


public class ViewGroupFragment extends DialogFragment implements Constants {

    @BindView(R.id.lvMembers)
    ListView lvMembers;

    @BindView(R.id.btnCreateEvent)
    Button btnCreateEvent;
    Group group;

    public static ViewGroupFragment newInstance(Parcelable group) {
        ViewGroupFragment fragment = new ViewGroupFragment();
        Bundle args = new Bundle();
        args.putParcelable(GROUP_OBJ, group);

       // args.putString(GROUP_NAME, groupName);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_group, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(group.getName());


        String[] values = new String[]{"Jennifer","Sonali"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        lvMembers.setAdapter(adapter);

        btnCreateEvent = (Button)view.findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //FragmentManager fm = getSupportFragmentManager();
                NewEventFragment eventFragment = NewEventFragment.newInstance(Parcels.wrap(group));
                eventFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                eventFragment.show(getFragmentManager(), "fragment_new_event");
                dismiss();
            }
        });
    }
}
