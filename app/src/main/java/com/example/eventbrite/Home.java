package com.example.eventbrite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbrite.Adapters.EventButtonTagAdapter;
import com.example.eventbrite.Adapters.EventItemAdapter;
import com.example.eventbrite.Models.Event;
import com.example.eventbrite.Services.EventServices;
import com.example.eventbrite.Services.OnEventsFetchedListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements OnEventsFetchedListener,EventButtonTagAdapter.OnTagClickListener {

    private EventButtonTagAdapter eventButtonTagAdapter;
    private EventItemAdapter eventItemAdapter;
    private ArrayList<Event> fetchedEventList;
    private ArrayList<Event> filteredEventList;
    private SearchView homeSearchView;
    private TextView seeAllTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set full-screen mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        RecyclerView eventButtonRecyclerView = (RecyclerView) findViewById(R.id.eventTagBtnRecyclerView);
        RecyclerView eventItemRecyclerView = (RecyclerView) findViewById(R.id.eventItemRecyvlerView);

        //Initialize event list
        fetchedEventList = new ArrayList<>();
        filteredEventList = new ArrayList<>(fetchedEventList);
        homeSearchView = (SearchView) findViewById(R.id.homeSearchView);
        seeAllTV = (TextView) findViewById(R.id.seeAllTV);

        //Initialize eventTagButtons adapter
        eventButtonTagAdapter = new EventButtonTagAdapter( this);
        eventItemAdapter = new EventItemAdapter(filteredEventList, this);


        //Set up the recycler views
        eventButtonRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventItemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventButtonRecyclerView.setAdapter(eventButtonTagAdapter);
        eventItemRecyclerView.setAdapter(eventItemAdapter);

        //Initialize EventServices and fetch events
        EventServices eventServices = new EventServices();
        eventServices.fetchAllEvents(this);

        homeSearchView.setOnClickListener(v -> NavigateToSearchView());
        seeAllTV.setOnClickListener(v -> NavigateToSeeAllEvents());

    }

    private void NavigateToSeeAllEvents() {
        Intent intent = new Intent(Home.this, AllFragmentsActivity.class);
        intent.putExtra("fetchedEventList", new ArrayList<>(fetchedEventList));

        //Fragment to load
        intent.putExtra("fragmentToLoad", "SeeAllEvents");
        startActivity(intent);
    }

    private void NavigateToSearchView() {
        Intent intent = new Intent(Home.this, AllFragmentsActivity.class);
        intent.putExtra("fetchedEventList", new ArrayList<>(fetchedEventList));

        //Fragment to load
        intent.putExtra("fragmentToLoad", "SearchFragment");
        startActivity(intent);
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onEventsFetched(List<Event> events) {
        Log.d("Home", "Fetched " + events.size() + " events");
        fetchedEventList.clear();
        fetchedEventList.addAll(events);

        //Clear and add events to filteredEventList
        filteredEventList.clear();
        filteredEventList.addAll(fetchedEventList); // Show all events initially

       //Update the adapters with new data
        eventButtonTagAdapter.updateEventList(fetchedEventList);
        eventItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEventsFetchFailed(String errorMessage) {
        Log.w("Home Activity", "Failed to fetch events" +errorMessage);

    }

    // Method from the EventButtonTagAdapter.OnTagClickListener interface
    @Override
    public void onTagClick(String tag) {
        filterEventsByTag(tag);
    }

    // Filtering logic based on the selected tag
    private void filterEventsByTag(String tag) {
        filteredEventList.clear(); // Clear the current filtered list
        for (Event event : fetchedEventList) {
            if (event.getEvent_tag().equals(tag)) {
                filteredEventList.add(event); // Add only the events that match the tag
            }
        }
        eventItemAdapter.notifyDataSetChanged(); // Update the RecyclerView with filtered events
    }
}