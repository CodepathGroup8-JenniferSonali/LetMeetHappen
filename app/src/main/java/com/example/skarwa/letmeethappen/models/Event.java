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
    long uid;
    String mEventName;
    Date mEventFinalDate;  //date the event is planned for after responses.
    Date mEventCreatedDate;  //date the event was created
    EventStatus mStatus;
    Group group;
    User planner;
    Location location;
    int minAcceptance;
    Date acceptByDate;
    EventStatus eventStatus;
    String plannerMsgToGroup;
    List<User> attendedUser;
    String hostProfileImage;  //TODO We should get the profile image from the user object ...but its fine for now.


    public void Event (Date date, String eventName) {
        mEventCreatedDate = date;
        mEventName = eventName;
        mStatus = EventStatus.NEW;
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
        event.mStatus = EventStatus.NEW;
        return event;
    }

    public long getUid() {
        return uid;
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
        return group;
    }

    public User getPlanner() {
        return planner;
    }

    public Location getLocation() {
        return location;
    }

    public int getMinAcceptance() {
        return minAcceptance;
    }

    public Date getAcceptByDate() {
        return acceptByDate;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public String getPlannerMsgToGroup() {
        return plannerMsgToGroup;
    }

    public List<User> getAttendedUser() {
        return attendedUser;
    }

    public String getHostProfileImage() {
        return hostProfileImage;
    }
}
