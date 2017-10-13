package com.example.skarwa.letmeethappen.models;

import java.util.Date;
import java.util.List;

/**
 * Created by skarwa on 10/12/17.
 */

public class Group {
    long uid;
    String name;
    Date createdDate;
    Date expiredDate;
    UserGroupStatus groupStatus;
    List<User> members;

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public UserGroupStatus getGroupStatus() {
        return groupStatus;
    }

    public List<User> getMembers() {
        return members;
    }
}
