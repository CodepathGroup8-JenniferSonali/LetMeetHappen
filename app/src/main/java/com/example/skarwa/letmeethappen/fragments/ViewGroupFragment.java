package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.adapters.GroupMembersAdapter;
import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.RetrieveDBToken;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ViewGroupFragment extends DialogFragment implements Constants {
    Group group;
    GroupMembersAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;

    @BindView(R.id.rvGroupMembers)
    RecyclerView rvGroupMembers;

    @BindView(R.id.btnCreateEvent)
    Button btnCreateEvent;

//    Group group;
 //   ArrayAdapter<String> adapter;


    // [START declare_database_ref]
    private DatabaseReference mGroupReference;
    // [END declare_database_ref]



    public static ViewGroupFragment newInstance(Parcelable group) {
        ViewGroupFragment fragment = new ViewGroupFragment();
        Bundle args = new Bundle();
        args.putParcelable(GROUP_OBJ, group);

        fragment.setArguments(args);
        return fragment;
    }

    private void initRecyclerView() {
        mAdapter = new GroupMembersAdapter(getContext(),mGroupReference);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.scrollToPosition(0);

        rvGroupMembers.setLayoutManager(linearLayoutManager);
        rvGroupMembers.addItemDecoration(itemDecoration);
        rvGroupMembers.setAdapter(mAdapter);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        group = Parcels.unwrap(args.getParcelable(GROUP_OBJ));
        new RetrieveDBToken().execute(group.getMembers().keySet().toArray());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_group, container, false);
        ButterKnife.bind(this,view);

        mGroupReference = FirebaseDatabase.getInstance().getReference().child("groups/"+group.getId()+"/members");

        initRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // getDialog().setTitle(group.getName());


        //Jennifer's Code'
/*
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

        lvMembers.setAdapter(adapter);*/


        btnCreateEvent = (Button)view.findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NewEventFragment eventFragment = NewEventFragment.newInstance(Parcels.wrap(group));
                eventFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                eventFragment.show(getFragmentManager(), "fragment_new_event");
                dismiss();
            }
        });
    }
}
