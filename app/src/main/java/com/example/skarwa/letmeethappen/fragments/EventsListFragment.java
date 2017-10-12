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

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.adapters.EventAdapter;
import com.example.skarwa.letmeethappen.models.Event;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by jennifergodinez on 10/2/17.
 */

public class EventsListFragment extends Fragment {
    private EventAdapter eventAdapter;
    private ArrayList<Event> events;
    private RecyclerView rvEvents;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        rvEvents = (RecyclerView) view.findViewById(R.id.rvEvents);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvEvents.addItemDecoration(itemDecoration);

        events = new ArrayList<Event>();

        eventAdapter = new EventAdapter(events);

        //temporary
        addItems(null);


        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvents.setAdapter(eventAdapter);

        return view;

    }

    public void addItems(JSONArray response) {
        //for (int i = 0; i < response.length(); i++) {
        for (int i = 0; i < 2; i++) {
            Event event = null;
            try {
                //event = Event.fromJSON(response.getJSONObject(i));
                event = Event.fromJSON(null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            events.add(event);
            eventAdapter.notifyItemInserted(events.size()-1);
        }
 
    }

}
