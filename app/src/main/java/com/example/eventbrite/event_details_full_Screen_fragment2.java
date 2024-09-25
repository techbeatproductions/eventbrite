package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventbrite.Models.Event;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link event_details_full_Screen_fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class event_details_full_Screen_fragment2 extends Fragment {

    TextView eventNameInEventDetails,actualEventDateInEventDetails, actualEventMainLocation, actualEventDetailedLocationInEventDetails, eventDescriptionInEventDetailsFrag;
    ImageView eventImageIV, backBtnIV;
    Button buyEventTicketBtnInEventDetailsFrag;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_EVENT = "event";
    private Event event;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public event_details_full_Screen_fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment event_details_full_Screen_fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static event_details_full_Screen_fragment2 newInstance(String param1, String param2) {
        event_details_full_Screen_fragment2 fragment = new event_details_full_Screen_fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(Event event) {
        event_details_full_Screen_fragment2 fragment2 = new event_details_full_Screen_fragment2();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT, event);
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable(ARG_EVENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_event_details_full__screen_fragment2, container, false);

        //Initialize the views
        //

        eventNameInEventDetails = (TextView) view.findViewById(R.id.eventNameInEventDetailsTV);
        actualEventDateInEventDetails = (TextView) view.findViewById(R.id.actualEventDateInEventDetailsTV);
        actualEventMainLocation = (TextView) view.findViewById(R.id.actualEventMainLocationTV);
        actualEventDetailedLocationInEventDetails = (TextView) view.findViewById(R.id.actualEventDetailedLocationInEventDetailsTV);
        eventDescriptionInEventDetailsFrag = (TextView) view.findViewById(R.id.eventDescriptionInEventDetailsFragTV);
        eventImageIV = (ImageView) view.findViewById(R.id.eventImageIV);
        backBtnIV = (ImageView) view.findViewById(R.id.backBtnIV); 
        buyEventTicketBtnInEventDetailsFrag = (Button) view.findViewById(R.id.buyEventTicketBtnInEventDetailsFrag);
        
        backBtnIV.setOnClickListener(v -> navigateBack());



        //Set the data to views if event is not null
        if (event != null){
            eventNameInEventDetails.setText(event.getEvent_name());
            actualEventDateInEventDetails.setText(event.getEvent_date());
            actualEventMainLocation.setText(event.getEvent_location());
            actualEventDetailedLocationInEventDetails.setText(event.getEvent_location());
            eventDescriptionInEventDetailsFrag.setText(event.getEvent_description());
            Glide.with(this).load(event.getEvent_image()).into(eventImageIV);

            // Set the button text to include the ticket price
            String buttonText = "Buy Ticket Kshs " + event.getEvent_ticket_price();
            buyEventTicketBtnInEventDetailsFrag.setText(buttonText);



        }

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