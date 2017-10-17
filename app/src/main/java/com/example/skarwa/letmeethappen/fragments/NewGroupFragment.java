package com.example.skarwa.letmeethappen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.MultiSpinner;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class NewGroupFragment extends DialogFragment {

    EditText etGroupName;
    ListView lvMembers;
    Button btn;
    MultiSpinner.MultiSpinnerListener listener;
    ArrayList<? extends Parcelable> friends;


    public static NewGroupFragment newInstance(ArrayList<? extends Parcelable>friends) {

        NewGroupFragment fragment = new NewGroupFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.FRIENDS_OBJ, friends);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        friends = (ArrayList<? extends Parcelable>)args.getParcelableArrayList(Constants.FRIENDS_OBJ);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_group, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MultiSpinner.MultiSpinnerListener)context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        etGroupName = (EditText)view.findViewById(R.id.etGroupName);

        List<String> items = new ArrayList<>();

        for(int i=0; i<friends.size(); i++) {
            User friend = Parcels.unwrap(friends.get(i));
            items.add(friend.getDisplayName());
        }
        MultiSpinner multiSpinner = (MultiSpinner) view.findViewById(R.id.multi_spinner);
        multiSpinner.setItems(items, getString(R.string.for_all),  listener);


        lvMembers = (ListView) view.findViewById(R.id.lvMembers);

        String[] values = new String[]{"Row1",
                "Row2",
                "Row 3",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        lvMembers.setAdapter(adapter);

        btn = (Button)view.findViewById(R.id.btnCreate);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // add new group to nav menu
                String groupName = etGroupName.getText().toString();
                if (groupName != null) {
                    //addMenuItemInNavMenuDrawer(view, groupName);
                }
                dismiss();
            }
        });

    }


    private void addMenuItemInNavMenuDrawer(View view, String groupName) {
        NavigationView navView = (NavigationView) view.findViewById(R.id.nvView);

        Menu menu = navView.getMenu();  //TODO: Debug NPE
        Menu submenu = menu.addSubMenu(groupName);
        navView.invalidate();
    }

}
