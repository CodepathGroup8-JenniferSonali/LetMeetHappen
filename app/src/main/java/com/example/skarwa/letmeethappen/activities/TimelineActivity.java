package com.example.skarwa.letmeethappen.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.adapters.EventsPagerAdapter;
import com.example.skarwa.letmeethappen.fragments.NewEventFragment;

/**
 * Created by jennifergodinez on 10/9/17.
 */

public class TimelineActivity extends AppCompatActivity {
    EventsPagerAdapter pagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        pagerAdapter = new EventsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                return true;

            case R.id.new_event:
                FragmentManager fm = getSupportFragmentManager();
                NewEventFragment eventFragment = NewEventFragment.newInstance("Some Title");
                eventFragment.show(fm, "fragment_new_event");
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
