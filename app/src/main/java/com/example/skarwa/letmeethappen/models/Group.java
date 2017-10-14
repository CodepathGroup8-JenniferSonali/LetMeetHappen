package com.example.skarwa.letmeethappen.models;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * Created by skarwa on 10/12/17.
 */

@Parcel
public class Group {
    long mid;
    String mName;
    Date mCreatedDate;
    Date mExpiredDate;
    UserGroupStatus mGroupStatus;
    List<User> mMembers;

    public Group() {
        //empty constructor needed
    }

    public void setId(long mid) {
        this.mid = mid;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setCreatedDate(Date mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
    }

    public void setExpiredDate(Date mExpiredDate) {
        this.mExpiredDate = mExpiredDate;
    }

    public void setGroupStatus(UserGroupStatus mGroupStatus) {
        this.mGroupStatus = mGroupStatus;
    }

    public void setMembers(List<User> mMembers) {
        this.mMembers = mMembers;
    }

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
