package com.example.skarwa.letmeethappen.models;

import com.google.android.gms.common.UserRecoverableException;

import org.parceler.Parcel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by skarwa on 10/12/17.
 */
@Parcel
public class UserMeetingPreferences {
    String mUser;
   // Event mEvent;
    Map<Date,UserResponse> mDatePreferenceMap;

    public UserMeetingPreferences() {
        //empty constructor needed
    }

    public void setUser(String userId) {
        this.mUser = mUser;
    }

    public void setDatePreferenceMap(Map<Date, UserResponse> mDatePreferenceMap) {
        this.mDatePreferenceMap = mDatePreferenceMap;
    }

    public String getUser() {
        return mUser;
    }

    public Map<Date, UserResponse> getDatePreferenceMap() {
        return mDatePreferenceMap;
    }
}
