package com.example.skarwa.letmeethappen.models;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Calendar;

import static java.lang.Thread.sleep;
import java.util.Date;
import java.util.List;

/**
 * Created by jennifergodinez on 9/25/17.
 */

public class Event {
    long mId;
    String mEventName;
    Date mEventFinalDate;  //date the event is planned for after responses.
    Date mEventCreatedDate;  //date the event was created
    Group mGroup;
    User mPlanner;
    Location mLocation;
    int mMinAcceptance;
    Date mAcceptByDate;
    EventStatus mEventStatus;
    String mPlannerMsgToGroup;
    List<User> mAttendedUser;
    String mHostProfileImage;  //TODO We should get the profile image from the user object ...but its fine for now.


    public void Event (Date date, String eventName) {
        mEventCreatedDate = date;
        mEventName = eventName;
        mEventStatus = EventStatus.NEW;
    }

    public static Event fromJSON(JSONObject jsonObject) throws JSONException {
        Event event = new Event();

        event.mEventCreatedDate = new Date(); //this will be today ...please change it as needed,
        event.mEventName = "Tea Party" +Calendar.getInstance().getTimeInMillis();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        event.mEventStatus = EventStatus.NEW;
        return event;
    }

    public long getId() {
        return mId;
    }

    public String getEventName() {
        return mEventName;
    }

    public Date getEventFinalDate() {
        return mEventFinalDate;
    }

    public Date getEventCreatedDate() {
        return mEventCreatedDate;
    }

    public Group getGroup() {
        return mGroup;
    }

    public User getPlanner() {
        return mPlanner;
    }

    public Location getLocation() {
        return mLocation;
    }

    public int getMinAcceptance() {
        return mMinAcceptance;
    }

    public Date getAcceptByDate() {
        return mAcceptByDate;
    }

    public EventStatus getEventStatus() {
        return mEventStatus;
    }

    public String getPlannerMsgToGroup() {
        return mPlannerMsgToGroup;
    }

    public List<User> getAttendedUser() {
        return mAttendedUser;
    }

    public String getHostProfileImage() {
        return mHostProfileImage;
    }
}
