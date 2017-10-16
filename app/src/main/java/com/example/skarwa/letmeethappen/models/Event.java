package com.example.skarwa.letmeethappen.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jennifergodinez on 9/25/17.
 */

@IgnoreExtraProperties
@Parcel
public class Event {
    long mId;
    String mEventName;
    String mEventFinalDate;  //date the event is planned for after responses.
    String mEventCreatedDate;  //date the event was created
    Group mGroup;
    User mPlanner;
    Location mLocation;
    int mMinAcceptance;
    String mAcceptByDate;
    EventStatus mEventStatus;
    String mPlannerMsgToGroup;
    List<User> mAttendedUser;
    List<String> mEventDateOptions;
    String mHostProfileImage;  //TODO We should get the profile image from the user object ...but its fine for now.


    public Event() {
        //empty constructor needed
    }

    public void Event (String date, String eventName) {
        mEventCreatedDate = date;
        mEventName = eventName;
        mEventStatus = EventStatus.NEW;

       // mEventDateOptions = new ArrayList<String>();
       // mAttendedUser = new ArrayList<User>();
    }

    public static Event fromJSON(JSONObject jsonObject) throws JSONException {
        Event event = new Event();

        event.mEventCreatedDate = new Date().toString(); //this will be today ...please change it as needed,
        event.mEventName = "Tea Party";

        event.mEventStatus = EventStatus.NEW;
        return event;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public void setEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public void setEventFinalDate(String mEventFinalDate) {
        this.mEventFinalDate = mEventFinalDate;
    }

    public void setEventCreatedDate(String mEventCreatedDate) {
        this.mEventCreatedDate = mEventCreatedDate;
    }

    public void setGroup(Group mGroup) {
        this.mGroup = mGroup;
    }

    public void setPlanner(User mPlanner) {
        this.mPlanner = mPlanner;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public void setMinAcceptance(int mMinAcceptance) {
        this.mMinAcceptance = mMinAcceptance;
    }

    public void setAcceptByDate(String mAcceptByDate) {
        this.mAcceptByDate = mAcceptByDate;
    }

    public void setPlannerMsgToGroup(String mPlannerMsgToGroup) {
        this.mPlannerMsgToGroup = mPlannerMsgToGroup;
    }

    public void addAttendedUser(User user) {
        if(this.mAttendedUser == null){
            this.mAttendedUser = new ArrayList<User>();
        }
        this.mAttendedUser.add(user);
    }

    public void setAttendedUser(List<User> users){
        this.mAttendedUser = users;
    }

    public void addEventDateOptions(String eventDateOption) {
        if(this.mEventDateOptions == null){
            this.mEventDateOptions =  new ArrayList<String>();
        }
        this.mEventDateOptions.add(eventDateOption);
    }


    public void setEventDateOptions(List<String> eventDateOptions){
        this.mEventDateOptions = eventDateOptions;
    }

    public void setHostProfileImage(String mHostProfileImage) {
        this.mHostProfileImage = mHostProfileImage;
    }

    public long getId() {
        return mId;
    }

    public String getEventName() {
        return mEventName;
    }

    public String getEventFinalDate() {
        return mEventFinalDate;
    }

    public String getEventCreatedDate() {
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

    public List<String> getEventDateOptions() {
        return mEventDateOptions;
    }

    public String getAcceptByDate() {
        return mAcceptByDate;
    }

    @Exclude
    public EventStatus getEventStatusAsEnum() {
        return mEventStatus;
    }

    public String getEventStatus() {
        // Convert enum to string
        if (mEventStatus == null) {
            return null;
        } else {
            return mEventStatus.name();
        }
    }

    public void setEventStatus(String event) {
        // Get enum from string
        if (event == null) {
            this.mEventStatus = null;
        } else {
            this.mEventStatus = EventStatus.valueOf(event);
        }
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
