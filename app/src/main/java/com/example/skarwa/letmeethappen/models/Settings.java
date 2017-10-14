package com.example.skarwa.letmeethappen.models;

import org.parceler.Parcel;

/**
 * Created by skarwa on 10/12/17.
 */

@Parcel
public class Settings {
    long mId;
    String mName;

    public long getUid() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
