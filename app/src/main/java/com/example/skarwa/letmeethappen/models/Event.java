package com.example.skarwa.letmeethappen.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static java.lang.Thread.sleep;

/**
 * Created by jennifergodinez on 9/25/17.
 */

public class Event {
    public String mDate;
    public long uid;
    public String mEventName;
    public EventStatus mStatus;
    public String hostProfileImage;

    public enum EventStatus {NEW, SET, CANCELED, PENDING};
    //public User planner;

    public void Event (String date, String eventName) {
        mDate = date;
        mEventName = eventName;
        mStatus = EventStatus.NEW;
    }
    

    public static Event fromJSON(JSONObject jsonObject) throws JSONException {
        Event event = new Event();

        event.mDate = "Oct 12, 2017";
        event.mEventName = "Tea Party" +Calendar.getInstance().getTimeInMillis();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        event.mStatus = EventStatus.NEW;


        return event;

    }


}
