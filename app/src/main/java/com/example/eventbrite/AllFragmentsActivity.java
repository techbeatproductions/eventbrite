package com.example.eventbrite;

import android.os.Bundle;
import android.util.Log; // Import logging
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventbrite.Models.Event;

import java.io.Serializable;
import java.util.ArrayList;

public class AllFragmentsActivity extends AppCompatActivity {
    private static final String TAG = "AllFragmentsActivity"; // For logging
    ArrayList<Event> passedEventListFromHomeActivity;
    Event selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_fragments);
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

        // Retrieve the fetched event list
        Serializable serializableExtra = getIntent().getSerializableExtra("fetchedEventList");
        selectedEvent = (Event) getIntent().getSerializableExtra("selectedEvent"); // Retrieve the selected event

        if (serializableExtra instanceof ArrayList<?>) {
            ArrayList<?> tempList = (ArrayList<?>) serializableExtra;
            if (!tempList.isEmpty() && tempList.get(0) instanceof Event) {
                passedEventListFromHomeActivity = (ArrayList<Event>) tempList;
            }
        }

        // Retrieve the identifier of the fragment to load
        String fragmentToLoad = getIntent().getStringExtra("fragmentToLoad");
        String userId = getIntent().getStringExtra("userId");

        // Load the appropriate fragment
        if (fragmentToLoad != null) {
            Log.d(TAG, "Fragment to load: " + fragmentToLoad); // Log the fragment being loaded
            switch (fragmentToLoad) {

                case "ProfilePhoto":
                    loadFragment(new ProfilePhotoFragment());
                    break;

                case "Notifications":
                    loadFragment(new NotificationFragment());
                    break;

                case "OrganizerProfile":
                    if (userId != null) {
                        loadFragment(OrganizerProfileFragment.newInstance(userId, passedEventListFromHomeActivity)); // Pass the user ID to the fragment
                    } else {
                        Log.e(TAG, "User ID is null for OrganizerProfile"); // Log an error if userId is null
                    }
                    break;

                case "SpecificEvent":
                    loadFragment(event_details_full_Screen_fragment2.newInstance(selectedEvent));
                    break;

                case "MyProfile":
                    loadFragment(new MyProfileFullFragment());
                    break;

                case "SeeAllEvents":
                    loadFragment(SeeAllEventsFragment.newInstance(passedEventListFromHomeActivity, true));
                    break;

                case "SearchFragment":
                    loadFragment(SearchWhiteBar.newInstance(passedEventListFromHomeActivity));
                    break;

                default:
                    Log.e(TAG, "Invalid fragment identifier: " + fragmentToLoad); // Log if the identifier is invalid
                    break;
            }
        } else {
            Log.e(TAG, "Fragment identifier is null!"); // Log if the identifier is null
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment == null) {
            Log.e(TAG, "Attempting to load a null fragment!"); // Log if the fragment is null
            return; // Avoid loading a null fragment
        }

        Log.d(TAG, "Loading fragment: " + fragment.getClass().getSimpleName()); // Log the fragment being loaded
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.allFragmentsContainerView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
