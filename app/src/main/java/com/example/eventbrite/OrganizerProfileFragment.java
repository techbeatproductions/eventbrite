package com.example.eventbrite;

import android.os.Bundle;
import android.util.Log; // Import logging utility

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import com.google.android.material.tabs.TabLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventbrite.Models.Event;
import com.example.eventbrite.Services.EventServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class OrganizerProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    private FragmentContainerView tabFragmentContainerView;
    private AboutFragment aboutFragment;
    private EventServices eventServices; // EventServices instance
    private ArrayList<Event> fetchedEventList;

    private static final String TAG = "OrganizerProfileFragment"; // Define tag for logging

    public OrganizerProfileFragment() {
        // Required empty public constructor
    }

    public static OrganizerProfileFragment newInstance(String param1, ArrayList<Event> eventList) {
        OrganizerProfileFragment fragment = new OrganizerProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable("eventList", eventList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("OrganizerProfileFragment", "onCreate called");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            // Retrieve the fetched events
            Serializable serializableExtra = getArguments().getSerializable("eventList");
            if (serializableExtra instanceof ArrayList<?>) {
                ArrayList<?> tempList = (ArrayList<?>) serializableExtra;
                if (!tempList.isEmpty() && tempList.get(0) instanceof Event) {
                    fetchedEventList = (ArrayList<Event>) tempList;
                    // Now you can use fetchedEventList as needed
                }
            }
        }
        eventServices = new EventServices(); // Initialize EventServices
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_profile, container, false);

        tabLayout = view.findViewById(R.id.tabLayoutOrganizerProfile);
        tabFragmentContainerView = view.findViewById(R.id.tabFragmentContainerView);

        // Initialize fragments
        aboutFragment = new AboutFragment();

        // Set the default fragment
        getChildFragmentManager().beginTransaction()
                .replace(R.id.tabFragmentContainerView, aboutFragment)
                .commit();

        // Set up tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0: // About tab
                        selectedFragment = aboutFragment;
                        break;
                    case 1: // Events tab
                        // Check if fetchedEventList is available before creating the fragment
                        if (fetchedEventList != null) {
                            Log.d(TAG, "Fetched events size: " + (fetchedEventList != null ? fetchedEventList.size() : 0));

                            SeeAllEventsFragment eventsFragment = SeeAllEventsFragment.newInstance(fetchedEventList, false);
                            selectedFragment = eventsFragment; // Pass the fetched events
                            Log.d(TAG, "EventsFragment created with events: " + fetchedEventList);
                        } else {
                            Log.d(TAG, "No events available to display.");
                        }
                        break;
                }
                // Replace the current fragment with the selected one
                if (selectedFragment != null) {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.tabFragmentContainerView, selectedFragment)
                            .commit();

                    Log.d(TAG, "Fragment replaced with: " + selectedFragment.getClass().getSimpleName());


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle unselected tab if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle reselected tab if needed
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("OrganizerProfileFragment", "onViewCreated called");
    }


}
