package com.example.skarwa.letmeethappen;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by jennifergodinez on 10/9/17.
 */

public class EventsPagerAdapter extends SmartFragmentStatePagerAdapter {
    private String[] tabTitles = {"UPCOMING", "Past"};
    private Context context;

    public EventsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        } else if (position == 1) {
            //return new PastTimelineFragment();
            return new HomeTimelineFragment(); //temporary
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

}