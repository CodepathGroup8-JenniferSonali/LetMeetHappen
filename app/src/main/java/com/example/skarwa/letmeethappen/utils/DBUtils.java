package com.example.skarwa.letmeethappen.utils;

import android.util.Log;

import com.example.skarwa.letmeethappen.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.example.skarwa.letmeethappen.utils.Constants.USERS_ENDPOINT;

/**
 * Created by skarwa on 10/22/17.
 */

public class DBUtils {
    static DatabaseReference mDatabase;
    public static HashMap<String, String> dbTokens;

    public static void saveUser(final User userToSave) {

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // run some code
                if (dataSnapshot.hasChild(USERS_ENDPOINT)) {
                    //if users already exists
                    mDatabase.child(USERS_ENDPOINT).child(userToSave.getId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                // code if user exists
                                User user = dataSnapshot.getValue(User.class);
                                Log.d(TAG, "User exist !!");
                            } else {
                                // user not found
                                Log.d(TAG, "User does not exist !!");
                                mDatabase.child(USERS_ENDPOINT).child(userToSave.getId()).setValue(userToSave);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                } else {  // if "users" child doesnt exist ..create the child and then add
                    DatabaseReference mUserDBReference = mDatabase.child(USERS_ENDPOINT).push();
                    mUserDBReference.child(userToSave.getId()).setValue(userToSave);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void updateUserGroup(final String userId, final User userToSave, final String groupKey) {

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mDatabase.child(USERS_ENDPOINT).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDatabase.child(USERS_ENDPOINT).child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            // code if user exists
                            User user = dataSnapshot.getValue(User.class);
                            mDatabase.child(USERS_ENDPOINT).child(userId).child("groups").child(groupKey).setValue(true);
                            Log.d(TAG, "User group update !!");
                        } else {
                            // user not found
                            Log.d(TAG, "User not found !!");
                            mDatabase.child(USERS_ENDPOINT).child(userId).setValue(userToSave);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
