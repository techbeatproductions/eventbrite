package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventbrite.Models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link event_details_full_Screen_fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class event_details_full_Screen_fragment2 extends Fragment {

    TextView eventNameInEventDetails,actualEventDateInEventDetails, actualEventMainLocation, actualEventDetailedLocationInEventDetails, eventDescriptionInEventDetailsFrag, actualEventTimeInEventDetailsTV, actualEventOrganizerNameInEventDetailsTV, actualEventOrganizerRoleInEventDetailsFragTV;
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

        actualEventTimeInEventDetailsTV = (TextView) view.findViewById(R.id.actualEventTimeInEventDetailsTV);
        actualEventOrganizerNameInEventDetailsTV = (TextView) view.findViewById(R.id.actualEventOrganizerNameInEventDetailsTV);
        actualEventOrganizerRoleInEventDetailsFragTV = (TextView) view.findViewById(R.id.actualEventOrganizerRoleInEventDetailsFragTV);


        
        backBtnIV.setOnClickListener(v -> navigateBack());



        //Set the data to views if event is not null
        if (event != null){
            eventNameInEventDetails.setText(event.getEvent_name());
            actualEventDateInEventDetails.setText(event.getEvent_date());
            actualEventMainLocation.setText(event.getEvent_location());
            actualEventDetailedLocationInEventDetails.setText(event.getEvent_location());
            eventDescriptionInEventDetailsFrag.setText(event.getEvent_description());
            Glide.with(this).load(event.getEvent_image()).into(eventImageIV);

            // Combine the event date and time, including the day of the week
            String eventDateTime = getFormattedDateWithDay(event.getEvent_date()) + " , " + event.getEvent_time();
            actualEventTimeInEventDetailsTV.setText(eventDateTime); // Set date, day of the week, and time
            
            
            // Set the button text to include the ticket price
            String buttonText = "Buy Ticket Kshs " + event.getEvent_ticket_price();
            buyEventTicketBtnInEventDetailsFrag.setText(buttonText);

            // Fetch and set the event organizer's name and role using the user ID
            fetchOrganizerDetails(event.getUser_id());



        }

        return view;
    }

    private String getFormattedDateWithDay(String eventDate) {
        // Remove suffix from the day (th, st, nd, rd)
        String cleanDate = eventDate.replaceAll("(\\d+)(st|nd|rd|th)", "$1");

        // Custom month names
        String[] months = new String[] {
                "Jan", "Feb", "Mar", "Apr", "May", "June",
                "July", "Aug", "Sept", "Oct", "Nov", "Dec"
        };

        // Replace the month with its corresponding number
        for (int i = 0; i < months.length; i++) {
            if (cleanDate.contains(months[i])) {
                // Replace month name with its numerical representation
                cleanDate = cleanDate.replace(months[i], String.valueOf(i + 1)); // Months are 1-based
            }
        }

        // The new cleanDate will now be in the format "26 9 2024"
        // Construct a new date string in a format compatible with SimpleDateFormat
        String[] parts = cleanDate.split(" ");
        String formattedDateString = parts[2] + "-" + parts[1] + "-" + parts[0]; // "2024-09-26"

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Format to parse
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE", Locale.getDefault()); // Desired output format: Day of the week
        String dayOfWeek = ""; // Default to empty in case of failure

        try {
            Date date = inputFormat.parse(formattedDateString);
            if (date != null) {
                dayOfWeek = outputFormat.format(date); // Format the date to output the day of the week
            }
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing error
        }

        return dayOfWeek; // Return the day of the week
    }



    private void fetchOrganizerDetails(String userId) {
        // Log the user ID to verify the correct ID is being used
        Log.d("EventDetails", "Fetching organizer details for user ID: " + userId);

        // Reference to the users node in Firebase
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // Assuming your user model has name and role fields
                    String organizerName = dataSnapshot.child("name").getValue(String.class);
                    String organizerRole = dataSnapshot.child("userType").getValue(String.class);

                    actualEventOrganizerNameInEventDetailsTV.setText(organizerName != null ? organizerName : "Organizer");
                    actualEventOrganizerRoleInEventDetailsFragTV.setText(organizerRole != null ? organizerRole : "Role not specified");

                    // Log the retrieved organizer details
                    Log.d("EventDetails", "Organizer Name: " + organizerName);
                    Log.d("EventDetails", "Organizer Role: " + organizerRole);
                } else {
                    // Handle case where user data is not found
                    actualEventOrganizerNameInEventDetailsTV.setText("Unknown Organizer");
                    actualEventOrganizerRoleInEventDetailsFragTV.setText("Role not available");

                    // Log that no data was found for the user
                    Log.w("EventDetails", "No data found for user ID: " + userId);
                }
            } else {
                // Handle any errors when fetching the data
                actualEventOrganizerNameInEventDetailsTV.setText("Error fetching organizer");
                actualEventOrganizerRoleInEventDetailsFragTV.setText("Role not available");

                // Log an error message if the task fails
                Log.e("EventDetails", "Error fetching organizer details for user ID: " + userId, task.getException());

            }

        }).addOnFailureListener(e -> {
            // Handle any failures in the request
            actualEventOrganizerNameInEventDetailsTV.setText("Error fetching organizer");
            actualEventOrganizerRoleInEventDetailsFragTV.setText("Role not available");

            // Log the failure message
            Log.e("EventDetails", "Failure fetching organizer details for user ID: " + userId + " - " + e.getMessage(), e);
        });
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