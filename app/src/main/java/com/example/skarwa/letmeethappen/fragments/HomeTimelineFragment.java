package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by jennifergodinez on 10/2/17.
 */

public class HomeTimelineFragment extends EventsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeline();

    }


    private void populateTimeline () {
        //addItems(null);
    }


}
