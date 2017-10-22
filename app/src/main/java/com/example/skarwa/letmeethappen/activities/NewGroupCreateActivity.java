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

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference mGroupDatabase;
    // [END declare_database_ref]
    String loggedInUserId;
    String loggedInTokenId;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // run some code
                if (dataSnapshot.hasChild(GROUPS_ENDPOINT)) {
                    mGroupDatabase =  mDatabase.child(GROUPS_ENDPOINT);
                    //if users already exists
                    mDatabase.child(GROUPS_ENDPOINT).addChildEventListener(new ChildEventListener() {
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
        loggedInTokenId = getIntent().getStringExtra(Constants.TOKEN_ID);
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

                //TODO verify the group name is unique
                group.setName(groupName);
                group.setCreatedDate(DateUtils.formatDateToString(new Date()));
                group.setGroupStatus(UserGroupStatus.ACTIVE.name());
                group.addMember(loggedInUserId, true);
                group.addToken(loggedInUserId, loggedInTokenId);
                for (final User member : members) {
                    group.addMember(member.getId(), true);
                    if (member.getTokenId() != null) {
                        group.addToken(member.getId(), member.getTokenId());
                    } else {
                        //TODO for testing only
                        if (member.getId().equals("jennifer,p1210")) {
                            //group.addToken(member.getId(), "fx9AjmDq4CI:APA91bGw6nlWSr8DWq5Shv4mCKY4nqOudAP7sb3Lm4tO5LZuTEVKR7tsDfKgNeddZqAKOmK5CdU1poyBQmEd420VAFyX1BPOM5BY8-L7E0RAf2kvMGeKveL6UekSKT1xd3HiQ0BqDilx");
                        }
                    }

                    FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // run some code
                            if (dataSnapshot.hasChild(USERS_ENDPOINT)) {
                                //if users already exists
                                mDatabase.child(USERS_ENDPOINT).child(member.getId()).addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // code if user exists
                                            User user = dataSnapshot.getValue(User.class);
                                            Log.d(TAG,"User exist !!");
                                        } else {
                                            // user not found
                                            Log.d(TAG,"User does not exist !!");
                                            mDatabase.child(USERS_ENDPOINT).child(member.getId()).setValue(member);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                });
                            } else {  // if "users" child doesnt exist ..create the child and then add
                                DatabaseReference mUserDBReference = mDatabase.child(USERS_ENDPOINT).push();
                                mUserDBReference.child(member.getId()).setValue(member);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


                if (groupName != null) {
                    //save group in DB

                    saveGroup();

                    //Add group  to NavDrawer


                    //open Nav Drawer to show newly added group
                    //  mDrawer.openDrawer(nvDrawer); //TODO



                    //addMenuItemInNavMenuDrawer(view, groupName);
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

                //TODO uncomment once we have proper user objects to save
              //  group.addMember(user.getId(),true);

            }
        }

        adapter.addAll(selects);
        adapter.notifyDataSetChanged();

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public void saveGroup(){

        String key = mGroupDatabase.push().getKey();
        group.setId(key);

        //TODO uncomment once we have proper user objects to save
                    /*for(User user : members){
                        user.getGroups().put(key,true);
                    }*/

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+GROUPS_ENDPOINT+"/" + key, group);
        childUpdates.put("/"+USERS_ENDPOINT+"/" +loggedInUserId+"/groups/"+key,true);

        //TODo update user object with the group info

        mDatabase.updateChildren(childUpdates);
    }


}
