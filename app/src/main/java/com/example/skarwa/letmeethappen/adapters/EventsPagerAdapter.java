package com.example.skarwa.letmeethappen.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.skarwa.letmeethappen.fragments.DraftEventsFragment;
import com.example.skarwa.letmeethappen.fragments.PastEventsFragment;
import com.example.skarwa.letmeethappen.fragments.PendingEventFragment;
import com.example.skarwa.letmeethappen.fragments.UpcomingEventsFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by jennifergodinez on 10/9/17.
 */

public class EventsPagerAdapter extends SmartFragmentStatePagerAdapter {
    private String[] tabTitles = {"UPCOMING", "PAST", "PENDING"};
    private Context context;

    public EventsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new UpcomingEventsFragment();
        } else if (position == 1) {
            //return new PastEventsFragment();
            return new PastEventsFragment();
        } else if (position == 2) {
            return new PendingEventFragment(); //temporary
        } else {
            return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 3; //3 if we support Draft/Saved New Event
    }

}
