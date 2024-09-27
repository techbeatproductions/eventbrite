package com.example.eventbrite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import com.google.android.material.tabs.TabLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventbrite.Models.Event;
import com.example.eventbrite.Services.EventServices;
import com.example.eventbrite.Services.OnEventsFetchedListener;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrganizerProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    private FragmentContainerView tabFragmentContainerView;
    private AboutFragment aboutFragment;
    private EventServices eventServices; // EventServices instance

    public OrganizerProfileFragment() {
        // Required empty public constructor
    }

    public static OrganizerProfileFragment newInstance(String param1, String param2) {
        OrganizerProfileFragment fragment = new OrganizerProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
                        selectedFragment = SeeAllEventsFragment.newInstance(); // Create new instance
                        fetchOrganizerEvents(((SeeAllEventsFragment) selectedFragment)); // Fetch events for the organizer
                        break;
                }
                // Replace the current fragment with the selected one
                if (selectedFragment != null) {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.tabFragmentContainerView, selectedFragment)
                            .commit();
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

    private void fetchOrganizerEvents(SeeAllEventsFragment eventsFragment) {
        // Use the user ID associated with the organizer to fetch their events
        String organizerUserId = mParam1; // Assuming mParam1 is the user ID of the organizer
        eventServices.fetchAllEvents(new OnEventsFetchedListener() {
            @Override
            public void onEventsFetched(List<Event> events) { // Make sure this parameter is List<Event>
                ArrayList<Event> userEvents = new ArrayList<>();
                for (Event event : events) { // This should work now if events is List<Event>
                    if (event.getUser_id().equals(organizerUserId)) {
                        userEvents.add(event);
                    }
                }
                // Set the fetched events in the SeeAllEventsFragment instance
                eventsFragment.setEventList(userEvents);
            }

            @Override
            public void onEventsFetchFailed(String errorMessage) {
                // Handle failure
            }
        });
    }



}
