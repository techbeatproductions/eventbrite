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
import com.example.eventbrite.Adapters.EventItemAdapter;
import com.example.eventbrite.Models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchWhiteBar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchWhiteBar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Event> eventListInSearchWhiteBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static  final String ARG_EVENT_LIST = "eventlist";
    private EventCardSearchViewItemAdapter eventItemAdapter;
    private RecyclerView recyclerView;

    public SearchWhiteBar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment SearchWhiteBar.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchWhiteBar newInstance(ArrayList<Event> passedEventListFromHomeActivity) {
        SearchWhiteBar fragment = new SearchWhiteBar();
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
        View view = inflater.inflate(R.layout.fragment_search_white_bar, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.eventsRecyclerViewInSearchWhiteBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView backBtn = (ImageView) view.findViewById(R.id.backBtnInSearchWhiteBar);

        //Initialize and set the adapter
        eventItemAdapter = new EventCardSearchViewItemAdapter(eventListInSearchWhiteBar, getContext());
        recyclerView.setAdapter(eventItemAdapter);

        backBtn.setOnClickListener(v -> navigateBack());
        return view;

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