package com.example.skarwa.letmeethappen;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jennifergodinez on 9/25/17.
 */

public class Event {
    public String date;
    public long uid;
    public String eventName;
    //public User planner;
    

    public static Event fromJSON(JSONObject jsonObject) throws JSONException {
        Event tweet = new Event();


        return tweet;

    }
}
