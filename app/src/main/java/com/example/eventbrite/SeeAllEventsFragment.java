package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

    private static final String ARG_SHOW_HEADER = "show_header";
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
    public static SeeAllEventsFragment newInstance(ArrayList<Event> passedEventListFromHomeActivity, boolean showHeader) {
        SeeAllEventsFragment fragment = new SeeAllEventsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_LIST, passedEventListFromHomeActivity);
        args.putBoolean(ARG_SHOW_HEADER, showHeader);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventListInSearchWhiteBar = (ArrayList<Event>) getArguments().getSerializable(ARG_EVENT_LIST);

            Log.d("SeeAllEventsFragment", "Event list size in onCreate: " +
                    (eventListInSearchWhiteBar != null ? eventListInSearchWhiteBar.size() : "null"));


        }
    }

    private void setHeaderVisibility(View view, boolean showHeader) {
        View headerLayout = view.findViewById(R.id.headerLayout);
        if (headerLayout != null) {
            headerLayout.setVisibility(showHeader ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_see_all_events, container, false);

        // Get the header visibility from arguments
        boolean showHeader = getArguments() != null && getArguments().getBoolean(ARG_SHOW_HEADER, true);
        setHeaderVisibility(view, showHeader);

        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerViewInSeeAllEvents);
        if (recyclerView == null) {
            Log.e("SeeAllEventsFragment", "RecyclerView is null!");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d("SeeAllEventsFragment", "RecyclerView visibility: " + recyclerView.getVisibility());


        ImageView backBtn = view.findViewById(R.id.backBtnSeeAllEventFragIV);
        ImageView searchBtn = view.findViewById(R.id.searchBtnIV);

        // Initialize and set the adapter
        Log.d("SeeAllEventsFragment", "Initializing adapter with event list: " + eventListInSearchWhiteBar);
        Log.d("SeeAllEventsFragment", "Event list size before initializing adapter: " + eventListInSearchWhiteBar.size());
        EventCardSearchViewItemAdapter eventItemAdapter = new EventCardSearchViewItemAdapter(eventListInSearchWhiteBar, getContext());
        recyclerView.setAdapter(eventItemAdapter);

        backBtn.setOnClickListener(v -> navigateBack());
        searchBtn.setOnClickListener(v -> navigateToSearch());

        Log.d("SeeAllEventsFragment", "Event list size in onCreateView: " +
                (eventListInSearchWhiteBar != null ? eventListInSearchWhiteBar.size() : "null"));

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
