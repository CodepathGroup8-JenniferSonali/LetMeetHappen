package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.skarwa.letmeethappen.models.Event;

/**
 * Created by jennifergodinez on 10/2/17.
 */

public class HomeTimelineFragment extends EventsListFragment {

    @Override
    void showEventDetail(Event event) {
        ViewEventFragment eventFragment = ViewEventFragment.newInstance(event);
        eventFragment.show(getFragmentManager(), "fragment_new_event");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeline();

    }


    private void populateTimeline () {
        //addItems(null);
    }


}
