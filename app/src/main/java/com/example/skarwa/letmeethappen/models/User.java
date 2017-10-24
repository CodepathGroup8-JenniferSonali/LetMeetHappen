package com.example.skarwa.letmeethappen.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by skarwa on 10/12/17.
 */

@IgnoreExtraProperties
@Parcel
public class User {
    String mId;
    String tokenId;
    String mDisplayName;
    String mEmail;
    String mPhoneNum;
    UserGroupStatus mUserStatus;
    String mProfilePicUrl;
    List<Settings> mUserSettings;
    Map<String,Boolean> mGroups;

    public User() {
        //default constructor needed by firebase
    }

    public static String encode(String email) {
        return email.replace(".", ",").replace("@gmail,com", "");
    }

    public static String decode(String id) {
        return id.replace(",", ".") + "@gmail,com";
    }


    public String getId() {
        return  encode(mEmail);
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
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

    @Exclude
    public UserGroupStatus getUserStatusAsEnum() {
        return mUserStatus;
    }

    public String getUserStatus() {
        // Convert enum to string
        if (mUserStatus == null) {
            return null;
        } else {
            return mUserStatus.name();
        }
    }

    public void setUserStatus(String status) {
        // Get enum from string
        if (status == null) {
            this.mUserStatus = null;
        } else {
            this.mUserStatus = UserGroupStatus.valueOf(status);
        }
    }


    public String getProfilePicUrl() {
        return mProfilePicUrl;
    }

    public List<Settings> getUserSettings() {
        return mUserSettings;
    }

    public Map<String, Boolean> getGroups() {
        return mGroups;
    }

    public void setGroups(Map<String, Boolean> groups) {
        this.mGroups = groups;
    }

    public void addGroup(String groupName,boolean isMember){
        if(this.mGroups == null){
            this.mGroups = new HashMap<>();
        }
        this.mGroups.put(groupName,isMember);
    }


}


