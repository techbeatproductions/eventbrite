package com.example.eventbrite;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.bumptech.glide.Glide;
import com.example.eventbrite.Models.Event;
import com.example.eventbrite.Services.EventServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Objects;

public class CreateEvent extends AppCompatActivity {

    TextView selectedEventImageTv,selectedEventDateTv, selectedEventTimeTv;
    TextInputEditText createEventTag,createEventName,createEventDescription,createEventLocation,createEventPrice;
    Button createEventBtn, selectEventImage, selectEventDate, selectEventTime;
    Uri imageUri;
    Calendar calendar;
    ImageView selectedEventImageIv;



    // Create an ActivityResultLauncher for picking an image
    ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    // Optionally, set the URI as text to the EditText
                    assert imageUri != null;
                    selectedEventImageTv.setText("Image Selected");
                    displaySelectedImage();

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createEventTag = (TextInputEditText) findViewById(R.id.createEventEventTagTextInputEditText);
        createEventName = (TextInputEditText) findViewById(R.id.createEventEventNameTextInputEditText);
        createEventDescription= (TextInputEditText) findViewById(R.id.createEventEventDescriptionTextInputEditText);
        createEventLocation= (TextInputEditText) findViewById(R.id.createEventEventLocationTextInputEditText);
        createEventPrice= (TextInputEditText) findViewById(R.id.createEventEventPriceTextInputEditText);
        createEventBtn = (Button) findViewById(R.id.createEventBtn);

        selectEventTime = (Button) findViewById(R.id.createEventSelectEventTimeBtn);
        selectedEventTimeTv = (TextView) findViewById(R.id.selectedEventTimeTv);

        selectEventDate= (Button) findViewById(R.id.createEventSelectEventDateBtn);
        selectEventImage= (Button) findViewById(R.id.createEventSelectEventImageBtn);
        selectedEventDateTv = (TextView) findViewById(R.id.selectedEventDateTv);
        selectedEventImageTv = (TextView) findViewById(R.id.selectedEventImageTv);
        selectedEventImageIv = findViewById(R.id.selectedEventImageIV);


        //Set up date picker for the event date field
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) ->{
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateField();
        };

        // Set up time picker
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            updateTimeField(); // Update time field
        };

        selectEventDate.setOnClickListener(v -> {
            new DatePickerDialog(CreateEvent.this, dateSetListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        selectEventTime.setOnClickListener(v -> {
            new TimePickerDialog(CreateEvent.this, timeSetListener,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

       // createEventImageClickable= (TextInputLayout) findViewById(R.id.createEventSelectEventImageBtn);

        selectEventImage.setOnClickListener(v -> openFileChooser());
        createEventBtn.setOnClickListener(v -> createEventFunction());
    }

    // Method to update the date in the TextInputEditText field
    private void updateDateField() {
        // Format the date as "25th September 2024"
        String formattedDate = getFormattedDateWithSuffix(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)
        );
        selectedEventDateTv.setText(formattedDate);
    }

    private void updateTimeField() {
        String formattedTime = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        selectedEventTimeTv.setText(formattedTime); // Display selected time
    }

    private void displaySelectedImage() {
        Glide.with(this)  // Use Glide to load the image
                .load(imageUri)
                .into(selectedEventImageIv);
    }


    private String getFormattedDateWithSuffix(int day, int month, int year) {
        // Get the month name in words
        String[] months = new String[] {
                "Jan", "Feb", "Mar", "Apr", "May", "June",
                "July", "Aug", "Sept", "Oct", "Nov", "Dec"
        };

        // Determine the suffix for the day
        String daySuffix;
        if (day >= 11 && day <= 13) {
            daySuffix = "th"; // Special case for 11th, 12th, 13th
        } else {
            switch (day % 10) {
                case 1:
                    daySuffix = "st";
                    break;
                case 2:
                    daySuffix = "nd";
                    break;
                case 3:
                    daySuffix = "rd";
                    break;
                default:
                    daySuffix = "th";
            }
        }

        // Return the formatted date string like "25th September 2024"
        return day + daySuffix + " " + months[month] + " " + year;
    }

    private void createEventFunction() {
        // Gather event data from the input fields
        String tag = Objects.requireNonNull(createEventTag.getText()).toString().trim();
        String name = Objects.requireNonNull(createEventName.getText()).toString().trim();
        String description = Objects.requireNonNull(createEventDescription.getText()).toString().trim();
        String location = Objects.requireNonNull(createEventLocation.getText()).toString().trim();
        String date = Objects.requireNonNull(selectedEventDateTv.getText()).toString().trim();
        String priceString = Objects.requireNonNull(createEventPrice.getText()).toString().trim();
        String time = Objects.requireNonNull(selectedEventTimeTv.getText()).toString().trim(); // Get selected time

        // Check if any field is empty
        if (name.isEmpty() || description.isEmpty() || location.isEmpty() || date.isEmpty() || time.isEmpty() || priceString.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the price string to double
        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the user ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create an event object and pass the image URI to the EventServices
        Event event = new Event(); // Assume you have an empty constructor in Event
        event.setEvent_tag(tag);
        event.setEvent_name(name);
        event.setEvent_description(description);
        event.setEvent_location(location);
        event.setEvent_date(date);
        event.setEvent_ticket_price(price);
        event.setUser_id(userId);
        event.setEvent_time(time);

        // Use EventServices to create the event
        EventServices eventServices = new EventServices();
        eventServices.createEvent(event, imageUri, new EventServices.EventCreationListener() {
            @Override
            public void onEventCreated() {
                Toast.makeText(CreateEvent.this, "Event created successfully!", Toast.LENGTH_SHORT).show();

                // Navigate back to Home activity
                Intent intent = new Intent(CreateEvent.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
                startActivity(intent);

                finish(); // Optionally, navigate back or clear the form
            }

            @Override
            public void onEventCreationFailed(String errorMessage) {
                Toast.makeText(CreateEvent.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void openFileChooser() {
        // Create an Intent to pick an image from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }



}