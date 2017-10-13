package com.example.skarwa.letmeethappen.models;

import com.google.android.gms.common.UserRecoverableException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by skarwa on 10/12/17.
 */

public class UserMeetingPreferences {
    User user;
    Event event;
    Map<Date,UserResponse> datePreferenceMap;

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public Map<Date, UserResponse> getDatePreferenceMap() {
        return datePreferenceMap;
    }
}
