package com.example.eventbrite;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.eventbrite.Models.Event;
import com.example.eventbrite.Services.EventServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class CreateEvent extends AppCompatActivity {

    TextInputLayout createEventImageClickable;
    TextInputEditText createEventTag,createEventName,createEventDescription,createEventImage,createEventLocation,createEventDate,createEventPrice;
    Button createEventBtn;
    Uri imageUri;
    Calendar calendar;



    // Create an ActivityResultLauncher for picking an image
    ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    // Optionally, set the URI as text to the EditText
                    assert imageUri != null;
                    createEventImage.setText(imageUri.toString());

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
        createEventImage= (TextInputEditText) findViewById(R.id.createEventEventImageTextInputEditText);
        createEventDate= (TextInputEditText) findViewById(R.id.createEventEventDateTextInputEditText);
        createEventLocation= (TextInputEditText) findViewById(R.id.createEventEventLocationTextInputEditText);
        createEventPrice= (TextInputEditText) findViewById(R.id.createEventEventPriceTextInputEditText);
        createEventBtn = (Button) findViewById(R.id.createEventButton);


        //Set up date picker for the event date field
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) ->{
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateField();
        };

        createEventDate.setOnClickListener(v -> {
            new DatePickerDialog(CreateEvent.this, dateSetListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        createEventImageClickable= (TextInputLayout) findViewById(R.id.createEventEventImageTextInputLayout);

        createEventImage.setOnClickListener(v -> openFileChooser());
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
        createEventDate.setText(formattedDate);
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
        String date = Objects.requireNonNull(createEventDate.getText()).toString().trim();
        String priceString = Objects.requireNonNull(createEventPrice.getText()).toString().trim();

        // Check if any field is empty
        if (name.isEmpty() || description.isEmpty() || location.isEmpty() || date.isEmpty() || priceString.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the price string to double
        double price = Double.parseDouble(priceString);

        // Create an event object and pass the image URI to the EventServices
        Event event = new Event(); // Assume you have an empty constructor in Event
        event.setEvent_tag(tag);
        event.setEvent_name(name);
        event.setEvent_description(description);
        event.setEvent_location(location);
        event.setEvent_date(date);
        event.setEvent_ticket_price(price);

        // Use EventServices to create the event
        EventServices eventServices = new EventServices();
        eventServices.createEvent(event, imageUri); // Pass the image URI along with the event
    }



    private void openFileChooser() {
        // Create an Intent to pick an image from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }



}