package com.example.skarwa.letmeethappen.models;

/**
 * Created by skarwa on 10/12/17.
 */

public enum EventStatus {
    SUCCESSFUL,  //if event was successful in the past
    CONFIRMED, //if upcoming event is confirmed.
    PENDING, //if event is awaiting response from others
    CANCELLED  //if event is cancelled.
}
