package com.example.skarwa.letmeethappen.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by skarwa on 10/12/17.
 */

@Parcel
public class User {
    String mId;
    String mDisplayName;
    String mEmail;
    String mPhoneNum;
    UserGroupStatus mUserStatus;
    String mProfilePicUrl;
    List<Settings> mUserSettings;

    public User() {
        //default constructor needed by firebase
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setPhoneNum(String mPhoneNum) {
        this.mPhoneNum = mPhoneNum;
    }

    public void setUserStatus(UserGroupStatus mUserStatus) {
        this.mUserStatus = mUserStatus;
    }

    public void setProfilePicUrl(String mProfilePicUrl) {
        this.mProfilePicUrl = mProfilePicUrl;
    }

    public void setUserSettings(List<Settings> mUserSettings) {
        this.mUserSettings = mUserSettings;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPhoneNum() {
        return mPhoneNum;
    }

    public UserGroupStatus getUserStatus() {
        return mUserStatus;
    }

    public String getProfilePicUrl() {
        return mProfilePicUrl;
    }

    public List<Settings> getUserSettings() {
        return mUserSettings;
    }
}


