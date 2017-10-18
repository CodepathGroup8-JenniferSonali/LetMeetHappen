package com.example.skarwa.letmeethappen.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class NewGroup_Activity extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {

    EditText etGroupName;
    ListView lvMembers;
    Button btn;
    MultiSpinner.MultiSpinnerListener listener;
    ArrayList<? extends Parcelable> friends;
    ArrayList<User> members;
    List<String> names;
    ArrayAdapter<String> adapter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        friends = (ArrayList<? extends Parcelable>) getIntent().getParcelableArrayListExtra(Constants.FRIENDS_OBJ);

        etGroupName = (EditText)findViewById(R.id.etGroupName);

        names = new ArrayList<>();

        for(int i=0; i<friends.size(); i++) {
            User friend = Parcels.unwrap(friends.get(i));
            names.add(friend.getDisplayName());
        }
        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.multi_spinner);
        multiSpinner.setItems(names, getString(R.string.for_all), this);


        lvMembers = (ListView) findViewById(R.id.lvMembers);

        String[] values = new String[]{"Row1",
                "Row2",
                "Row 3",
        };

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        lvMembers.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        btn = (Button)findViewById(R.id.btnCreate);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // add new group to nav menu
                String groupName = etGroupName.getText().toString();
                if (groupName != null) {
                    //addMenuItemInNavMenuDrawer(view, groupName);
                }
                finish();
            }
        });

    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        Log.d("DEBUG", "onContacts selected");


        ArrayList<String> selects = new ArrayList<>();
        members = new ArrayList<User>();


        for (int i=0; i < selected.length; i++) {
            if (selected[i]) {
                selects.add(names.get(i));
                members.add((User)Parcels.unwrap(friends.get(i)));

            }
        }

        adapter.addAll(selects);

    }
}
