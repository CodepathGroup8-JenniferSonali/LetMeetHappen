package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewGroupFragment extends DialogFragment implements Constants {

    @BindView(R.id.lvMembers)
    ListView lvMembers;

    @BindView(R.id.btnCreateEvent)
    Button btnCreateEvent;
    Group group;
    ArrayAdapter<String> adapter;

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

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());


        Map<String, Boolean> members = group.getMembers();

        ArrayList<String> memberNames = new ArrayList<>();

        if (members != null) {
            for (String tid : members.keySet()) {

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child(Constants.USERS_ENDPOINT)
                        .child(tid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                Log.d("d e  b  u  g ", "onDataChange");
                                if (user != null) {
                                    adapter.add(user.getDisplayName());
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("d e  b  u  g ", "onCancelled");
                            }
                        });
                // exclude the host
                //TODO get loggedInUser if (tId != loggedInUser.getId()) {
                //Member m = members.get(tid);
                //memberNames.add(m.getName());

            }
        }

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
