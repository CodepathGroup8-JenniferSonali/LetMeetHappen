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
    User mUser;
    Event mEvent;
    Map<Date,UserResponse> mDatePreferenceMap;

    public UserMeetingPreferences() {
        //empty constructor needed
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public void setEvent(Event mEvent) {
        this.mEvent = mEvent;
    }

    public void setDatePreferenceMap(Map<Date, UserResponse> mDatePreferenceMap) {
        this.mDatePreferenceMap = mDatePreferenceMap;
    }

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
