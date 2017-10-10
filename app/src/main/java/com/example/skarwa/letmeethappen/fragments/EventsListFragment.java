package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.adapters.EventAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by jennifergodinez on 10/2/17.
 */

public class EventsListFragment extends Fragment {
    private EventAdapter tweetAdapter;
    private ArrayList<Event> tweets;
    private RecyclerView rvEvents;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetup_list, container, false);

        rvEvents = (RecyclerView) view.findViewById(R.id.rvMeetup);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvEvents.addItemDecoration(itemDecoration);

        tweets = new ArrayList<>();
        tweetAdapter = new EventAdapter(tweets);

        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvents.setAdapter(tweetAdapter);

        return view;

    }

    public void addItems(JSONArray response) {
 
    }

}
