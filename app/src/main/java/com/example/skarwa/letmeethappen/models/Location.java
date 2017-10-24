package com.example.skarwa.letmeethappen.models;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

/**
 * Created by skarwa on 10/12/17.
 */

@Parcel
public class Location {
    double mLatitude;
    double mLongitude;
    String mName;

    public Location() {
        //empty constructor needed
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getName() {
        return mName;
    }
}
