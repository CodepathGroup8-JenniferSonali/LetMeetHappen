package com.example.skarwa.letmeethappen.models;

import java.util.Date;
import java.util.List;

/**
 * Created by skarwa on 10/12/17.
 */

public class Group {
    long mid;
    String mName;
    Date mCreatedDate;
    Date mExpiredDate;
    UserGroupStatus mGroupStatus;
    List<User> mMembers;

    public long getId() {
        return mid;
    }

    public String getName() {
        return mName;
    }

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public Date getExpiredDate() {
        return mExpiredDate;
    }

    public UserGroupStatus getGroupStatus() {
        return mGroupStatus;
    }

    public List<User> getMembers() {
        return mMembers;
    }
}
