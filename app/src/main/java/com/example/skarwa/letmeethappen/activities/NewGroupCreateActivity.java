package com.example.skarwa.letmeethappen.activities;

import android.content.Intent;
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
import com.example.skarwa.letmeethappen.models.Group;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.models.UserGroupStatus;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.DateUtils;
import com.example.skarwa.letmeethappen.utils.MultiSpinner;
import com.example.skarwa.letmeethappen.utils.DBUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.skarwa.letmeethappen.utils.Constants.GROUPS_ENDPOINT;
import static com.example.skarwa.letmeethappen.utils.Constants.USERS_ENDPOINT;

public class NewGroupCreateActivity extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {

    private static final String TAG = "NewGroupCreateActivity";
    EditText etGroupName;
    ListView lvMembers;
    Button btn;
    MultiSpinner.MultiSpinnerListener listener;
    ArrayList<? extends Parcelable> friends;
    ArrayList<User> members;
    List<String> names;
    ArrayAdapter<String> adapter;
    Group group;
    DBUtils DBUtils;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference mGroupDatabase;
    private DatabaseReference mUserDatabase;
    // [END declare_database_ref]
    String loggedInUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DBUtils = new DBUtils();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // run some code
                if (dataSnapshot.hasChild(GROUPS_ENDPOINT)) {
                    //if users already exists
                   mGroupDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Group newGroup = dataSnapshot.getValue(Group.class);
                            if(newGroup != null){
                                Log.d(TAG,"New Group created : "+newGroup.getName());
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                } else {  // if "group" child doesnt exist ..create the child and then add
                    mGroupDatabase = mDatabase.child(GROUPS_ENDPOINT).push();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // [END initialize_database_ref]
        loggedInUserId = getIntent().getStringExtra(Constants.USER_ID);
        group = new Group();

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

                //TODO verify the group name is unique
                group.setName(groupName);
                group.setCreatedDate(DateUtils.formatDateToString(new Date()));
                group.setGroupStatus(UserGroupStatus.ACTIVE.name());
                group.addMember(loggedInUserId, true);
                for (final User member : members) {
                    group.addMember(member.getId(), true);
                    DBUtils.saveUser(member);
                }


                if (groupName != null) {
                    saveGroup();
                    sendInvite(group);
                }

                Intent intent = new Intent();
                intent.putExtra("new_group", Parcels.wrap(group));
                setResult(RESULT_OK, intent);

                finish();
            }
        });

    }



    @Override
    public void onItemsSelected(boolean[] selected) {
        Log.d("DEBUG", "onContacts selected");


        ArrayList<String> selects = new ArrayList<>();
        members = new ArrayList<User>();
        adapter.clear();


        for (int i=0; i < selected.length; i++) {
            if (selected[i]) {
                User user = (User) Parcels.unwrap(friends.get(i));

                selects.add(names.get(i));
                members.add(user);
            }
        }

        adapter.addAll(selects);

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public void saveGroup(){
        mGroupDatabase = mDatabase.child(GROUPS_ENDPOINT);

        String key = mGroupDatabase.push().getKey();
        group.setId(key);

        Map<String, Object> childUpdates = new HashMap<>();

        //update group for loggedInUser
        childUpdates.put("/"+USERS_ENDPOINT+"/" +loggedInUserId+"/groups/"+key,true);

        //Save Group
        childUpdates.put("/"+GROUPS_ENDPOINT+"/" + key, group);

        //Save group members
        for(User user : members){
            //user.getGroups().put(key,true);

            //mGroupDatabase.child(USERS_ENDPOINT).child(user.getId()).setValue(user);
           childUpdates.put("/"+USERS_ENDPOINT+"/" +user.getId()+"/groups/"+key,true);
        }
        mDatabase.updateChildren(childUpdates);
    }

    public void sendInvite(Group group) {
    // send invites to all group members to join

    }
}
