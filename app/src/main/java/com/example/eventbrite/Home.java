package com.example.eventbrite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements OnEventsFetchedListener, EventButtonTagAdapter.OnTagClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1; // Define a request code for the location permission
    private EventButtonTagAdapter eventButtonTagAdapter;
    private EventItemAdapter eventItemAdapter;
    private ArrayList<Event> fetchedEventList;
    private ArrayList<Event> filteredEventList;
    private SearchView homeSearchView;
    private TextView seeAllTV;
    private TextView actualLocationTv; // TextView for displaying location
    private FusedLocationProviderClient fusedLocationClient;
    private BottomNavigationView bottomNavigationView;

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

        RecyclerView eventButtonRecyclerView = findViewById(R.id.eventTagBtnRecyclerView);
        RecyclerView eventItemRecyclerView = findViewById(R.id.eventItemRecyvlerView);
        actualLocationTv = findViewById(R.id.actualLocationTv); // Initialize TextView for location
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Initialize event list
        fetchedEventList = new ArrayList<>();
        filteredEventList = new ArrayList<>(fetchedEventList);
        homeSearchView = findViewById(R.id.homeSearchView);
        seeAllTV = findViewById(R.id.seeAllTV);

        // Initialize adapters
        eventButtonTagAdapter = new EventButtonTagAdapter(this);
        eventItemAdapter = new EventItemAdapter(filteredEventList, this);

        // Set up the recycler views
        eventButtonRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventItemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventButtonRecyclerView.setAdapter(eventButtonTagAdapter);
        eventItemRecyclerView.setAdapter(eventItemAdapter);

        // Initialize EventServices and fetch events
        EventServices eventServices = new EventServices();
        eventServices.fetchAllEvents(this);

        homeSearchView.setOnClickListener(v -> NavigateToSearchView());
        seeAllTV.setOnClickListener(v -> NavigateToSeeAllEvents());

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are already granted, you can access location
            getCurrentLocation();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.explore) {
                // Navigate to Explore Activity or Fragment
                startActivity(new Intent(Home.this, Home.class)); // This seems to be the same activity, you might want to change it
                return true;
            } else if (item.getItemId() == R.id.events) {
                // Navigate to AllFragmentsActivity and load SeeAllEventsFragment
                Intent eventsIntent = new Intent(Home.this, AllFragmentsActivity.class);
                eventsIntent.putExtra("fetchedEventList", new ArrayList<>(fetchedEventList));
                eventsIntent.putExtra("fragmentToLoad", "SeeAllEvents");
                startActivity(eventsIntent);
                return true;
            } else if (item.getItemId() == R.id.profile) {
                // Navigate to AllFragmentsActivity and load MyProfileFullFragment
                Intent profileIntent = new Intent(Home.this, AllFragmentsActivity.class);
                profileIntent.putExtra("fetchedEventList", new ArrayList<>(fetchedEventList));
                profileIntent.putExtra("fragmentToLoad", "MyProfile");
                startActivity(profileIntent);
                return true;
            }

            return false; // Return false if no item matched
        });

    }

    private void NavigateToSeeAllEvents() {
        Intent intent = new Intent(Home.this, AllFragmentsActivity.class);
        intent.putExtra("fetchedEventList", new ArrayList<>(fetchedEventList));

        // Fragment to load
        intent.putExtra("fragmentToLoad", "SeeAllEvents");
        startActivity(intent);
    }

    private void NavigateToSearchView() {
        Intent intent = new Intent(Home.this, AllFragmentsActivity.class);
        intent.putExtra("fetchedEventList", new ArrayList<>(fetchedEventList));

        // Fragment to load
        intent.putExtra("fragmentToLoad", "SearchFragment");
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getCurrentLocation();
            } else {
                // Permission denied, handle appropriately (e.g., show a message)
                Log.w("Home", "Location permission denied");
                actualLocationTv.setText("Location permission denied");
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                Log.d("Home", "Current location: " + latitude + ", " + longitude);

                                // Get the city and country from the location
                                Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());
                                try {
                                    List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    if (addresses != null && !addresses.isEmpty()) {
                                        String city = addresses.get(0).getLocality(); // Get city name
                                        String country = addresses.get(0).getCountryName(); // Get country name
                                        actualLocationTv.setText("Location: " + city + ", " + country);
                                    } else {
                                        actualLocationTv.setText("Location not available");
                                    }
                                } catch (Exception e) {
                                    Log.e("Home", "Geocoder error: " + e.getMessage());
                                    actualLocationTv.setText("Location not available");
                                }
                            } else {
                                Log.w("Home", "Location is null");
                                actualLocationTv.setText("Location not available");
                            }
                        }
                    });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onEventsFetched(List<Event> events) {
        Log.d("Home", "Fetched " + events.size() + " events");
        fetchedEventList.clear();
        fetchedEventList.addAll(events);

        // Clear and add events to filteredEventList
        filteredEventList.clear();
        filteredEventList.addAll(fetchedEventList); // Show all events initially

        // Update the adapters with new data
        eventButtonTagAdapter.updateEventList(fetchedEventList);
        eventItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEventsFetchFailed(String errorMessage) {
        Log.w("Home Activity", "Failed to fetch events" + errorMessage);
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
