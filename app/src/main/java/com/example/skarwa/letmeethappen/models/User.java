package com.example.skarwa.letmeethappen.models;

import java.util.Date;
import java.util.List;

/**
 * Created by skarwa on 10/12/17.
 */

public class User {
    long mId;
    String mFirstName;
    String mLastName;
    String mEmail;
    String mPhoneNum;
    String mEncryptedPwd;  //TODO encrypt password
    Date mJoinedDate;
    UserGroupStatus mUserStatus;
    String mProfilePicUrl;
    List<Settings> mUserSettings;

    public long getId() {
        return mId;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPhoneNum() {
        return mPhoneNum;
    }

    public String getEncryptedPwd() {
        return mEncryptedPwd;
    }

    public Date getJoinedDate() {
        return mJoinedDate;
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
