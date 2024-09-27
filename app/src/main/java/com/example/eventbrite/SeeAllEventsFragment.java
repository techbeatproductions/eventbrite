package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eventbrite.Adapters.EventCardSearchViewItemAdapter;
import com.example.eventbrite.Models.Event;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeeAllEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeeAllEventsFragment extends Fragment {

    private static final String ARG_EVENT_LIST = "eventlist";
    private ArrayList<Event> eventListInSearchWhiteBar;

    public SeeAllEventsFragment() {
        // Required empty public constructor
    }

    // Factory method to create an instance of the fragment
    public static SeeAllEventsFragment newInstance() {
        return new SeeAllEventsFragment();
    }

    // Method to create a new instance with an event list
    public static SeeAllEventsFragment newInstance(ArrayList<Event> passedEventListFromHomeActivity) {
        SeeAllEventsFragment fragment = new SeeAllEventsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_LIST, passedEventListFromHomeActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventListInSearchWhiteBar = (ArrayList<Event>) getArguments().getSerializable(ARG_EVENT_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_see_all_events, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerViewInSeeAllEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView backBtn = view.findViewById(R.id.backBtnSeeAllEventFragIV);
        ImageView searchBtn = view.findViewById(R.id.searchBtnIV);

        // Initialize and set the adapter
        EventCardSearchViewItemAdapter eventItemAdapter = new EventCardSearchViewItemAdapter(eventListInSearchWhiteBar, getContext());
        recyclerView.setAdapter(eventItemAdapter);

        backBtn.setOnClickListener(v -> navigateBack());
        searchBtn.setOnClickListener(v -> navigateToSearch());

        return view;
    }

    private void navigateToSearch() {
        Intent intent = new Intent(getContext(), AllFragmentsActivity.class);
        intent.putExtra("fetchedEventList", new ArrayList<>(eventListInSearchWhiteBar));

        // Fragment to load
        intent.putExtra("fragmentToLoad", "SearchFragment");
        startActivity(intent);
    }

    private void navigateBack() {
        // Create an Intent to navigate back to the Home Activity
        Intent intent = new Intent(getActivity(), Home.class);
        // Optionally, you can add flags to clear the back stack if needed
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    // New method to set the event list directly (if needed)
    public void setEventList(ArrayList<Event> eventList) {
        this.eventListInSearchWhiteBar = eventList;
        // Notify the adapter if you have one set
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.eventsRecyclerViewInSeeAllEvents);
            EventCardSearchViewItemAdapter adapter = (EventCardSearchViewItemAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.notifyDataSetChanged(); // Notify adapter about data change
            }
        }
    }
}
