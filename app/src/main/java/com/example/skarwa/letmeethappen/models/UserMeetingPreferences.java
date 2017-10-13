package com.example.skarwa.letmeethappen.models;

import com.google.android.gms.common.UserRecoverableException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by skarwa on 10/12/17.
 */

public class UserMeetingPreferences {
    User mUser;
    Event mEvent;
    Map<Date,UserResponse> mDatePreferenceMap;

    public User getUser() {
        return mUser;
    }

    public Event getEvent() {
        return mEvent;
    }

    public Map<Date, UserResponse> getDatePreferenceMap() {
        return mDatePreferenceMap;
    }
}
