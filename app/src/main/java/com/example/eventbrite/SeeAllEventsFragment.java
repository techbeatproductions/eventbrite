package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Event> eventListInSearchWhiteBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static  final String ARG_EVENT_LIST = "eventlist";

    public SeeAllEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeeAllEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeeAllEventsFragment newInstance(String param1, String param2) {
        SeeAllEventsFragment fragment = new SeeAllEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(ArrayList<Event> passedEventListFromHomeActivity) {
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.eventsRecyclerViewInSeeAllEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView backBtn = (ImageView) view.findViewById(R.id.backBtnSeeAllEventFragIV);
        ImageView searchBtn = (ImageView) view.findViewById(R.id.searchBtnIV);

        //Initialize and set the adapter
        EventCardSearchViewItemAdapter eventItemAdapter = new EventCardSearchViewItemAdapter(eventListInSearchWhiteBar, getContext());
        recyclerView.setAdapter(eventItemAdapter);

        backBtn.setOnClickListener(v -> navigateBack());
        searchBtn.setOnClickListener(v -> navigateToSearch());

        return view;
    }

    private void navigateToSearch() {
        Intent intent = new Intent(getContext(), AllFragmentsActivity.class);
        intent.putExtra("fetchedEventList", new ArrayList<>(eventListInSearchWhiteBar));

        //Fragment to load
        intent.putExtra("fragmentToLoad", "SearchFragment");
        startActivity(intent);
    }

    private void navigateBack() {
        // Create an Intent to navigate back to the Home Activity
        Intent intent = new Intent(getActivity(), Home.class);
        // Optionally, you can add flags to clear the back stack if needed
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // Finish the current fragment or activity if necessary
        requireActivity().finish();
    }
}