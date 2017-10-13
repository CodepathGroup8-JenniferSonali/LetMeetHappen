package com.example.skarwa.letmeethappen.models;

import java.util.Date;
import java.util.List;

/**
 * Created by skarwa on 10/12/17.
 */

public class User {
    long uid;
    String firstName;
    String lastName;
    String email;
    String phoneNum;
    String encryptedPwd;  //TODO encrypt password
    Date joinedDate;
    UserGroupStatus userStatus;
    String profilePicUrl;
    List<Settings> userSettings;

    public long getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEncryptedPwd() {
        return encryptedPwd;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public UserGroupStatus getUserStatus() {
        return userStatus;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public List<Settings> getUserSettings() {
        return userSettings;
    }
}
