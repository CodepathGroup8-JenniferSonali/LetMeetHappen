package com.example.skarwa.letmeethappen.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.skarwa.letmeethappen.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.skarwa.letmeethappen.utils.DBUtils.mDatabase;

/**
 * Created by jennifergodinez on 10/26/17.
 */


public class RetrieveDBToken extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] iDs) {

        for (int i=0; i< iDs.length; i++) {

            final String userId = (String)iDs[i];


            FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(Constants.USERS_ENDPOINT)) {
                        //if users already exists
                        mDatabase.child(Constants.USERS_ENDPOINT).child(userId).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // code if user exists
                                    User user = dataSnapshot.getValue(User.class);
                                    Log.d("DEBUG", "User exist !!");
                                    if (user.getTokenId() != null) {
                                        if (DBUtils.dbTokens == null) {
                                            DBUtils.dbTokens = new HashMap<String, String>();
                                        }
                                        DBUtils.dbTokens.put(user.getId(), user.getTokenId());
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return null;
    }
}

