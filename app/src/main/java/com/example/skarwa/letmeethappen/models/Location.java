package com.example.skarwa.letmeethappen.models;

import org.parceler.Parcel;

/**
 * Created by skarwa on 10/12/17.
 */

@Parcel
public class Location {
    float mLatitude;
    float mLongitude;
    String mUserFriendlyName;

    public Location() {
        //empty constructor needed
    }

    public void setLatitude(float mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setLongitude(float mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setUserFriendlyName(String mUserFriendlyName) {
        this.mUserFriendlyName = mUserFriendlyName;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public String getUserFriendlyName() {
        return mUserFriendlyName;
    }


}
