package com.example.eventbrite;

import android.os.Bundle;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AllFragmentsActivity extends AppCompatActivity {
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

        Serializable serializableExtra = getIntent().getSerializableExtra("fetchedEventList");
        selectedEvent = (Event) getIntent().getSerializableExtra("selectedEvent"); // Retrieve the selected event

        if (serializableExtra instanceof ArrayList<?>) {
            ArrayList<?> tempList = (ArrayList<?>) serializableExtra;
            if (!tempList.isEmpty() && tempList.get(0) instanceof Event) {
                // Now it's safe to cast to ArrayList<Event>
                passedEventListFromHomeActivity = (ArrayList<Event>) tempList;
            }
        }

        //Retrieve the identifier of the fragment to load
        String fragmentToLoad = getIntent().getStringExtra("fragmentToLoad");

        //Load the appropriate fragment
        if (fragmentToLoad != null){
            switch (fragmentToLoad){
                case "OrganizerProfile":
                    loadFragment(OrganizerProfileFragment.newInstance());
                    break;

                case "SpecificEvent":
                    loadFragment(event_details_full_Screen_fragment2.newInstance(selectedEvent));
                    break;

                case "MyProfile":
                    loadFragment(new MyProfileFullFragment());
                    break;

                case "SeeAllEvents":
                    loadFragment(SeeAllEventsFragment.newInstance(passedEventListFromHomeActivity));
                    break;



                case "SearchFragment":
                    loadFragment(SearchWhiteBar.newInstance(passedEventListFromHomeActivity));
                    break;




            }
        }

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.allFragmentsContainerView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}