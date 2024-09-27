package com.example.eventbrite.Services;

import android.net.Uri;
import android.util.Log;

import com.example.eventbrite.Models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EventServices {
    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    public EventServices() {
        // Initialize Firebase Realtime Database reference
        this.databaseRef = FirebaseDatabase.getInstance().getReference("events");

        // Initialize Firebase Storage reference
        this.storageRef = FirebaseStorage.getInstance().getReference("events_images");
    }

    // Event creation listener interface
    public interface EventCreationListener {
        void onEventCreated();
        void onEventCreationFailed(String errorMessage);
    }

    // Create Event with image upload
    public void createEvent(Event event, Uri imageUri, EventCreationListener listener) {
        String eventId = databaseRef.push().getKey();
        if (eventId != null) {
            event.setEvent_id(eventId);
            uploadImage(imageUri, eventId, event, listener);
        } else {
            listener.onEventCreationFailed("Failed to generate event ID");
        }
    }

    private void uploadImage(Uri imageUri, String eventId, Event event, EventCreationListener listener) {
        StorageReference fileReference = storageRef.child(eventId + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        event.setEvent_image(uri.toString()); // Set the image URL in the event object
                        // Now save the event to the database
                        saveEventToDatabase(event, listener);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.w("FirebaseStorage", "Failed to upload image: " + e.getMessage());
                    listener.onEventCreationFailed("Image upload failed: " + e.getMessage());
                });
    }

    private void saveEventToDatabase(Event event, EventCreationListener listener) {
        databaseRef.child(event.getEvent_id()).setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseDB", "Event created successfully");
                    listener.onEventCreated();
                })
                .addOnFailureListener(e -> {
                    Log.w("FirebaseDB", "Failed to create event", e);
                    listener.onEventCreationFailed("Failed to create event: " + e.getMessage());
                });
    }

    // Fetch event
    public void fetchEvent(String eventId, final onEventFetchedListener listener) {
        databaseRef.child(eventId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // Convert the DataSnapshot to an event object
                    Event event = dataSnapshot.getValue(Event.class);
                    listener.onEventFetched(event);
                } else {
                    Log.w("FirebaseDB", "No event found with Id: " + eventId);
                    listener.onEventFetchFailed("Event not found");
                }
            } else {
                Log.w("FirebaseDB", "Failed to fetch event", task.getException());
                listener.onEventFetchFailed(task.getException().getMessage());
            }
        });
    }



    public void fetchAllEvents(OnEventsFetchedListener listener) {
        databaseRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                List<Event> eventList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convert each child DataSnapshot to an Event object
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }

                listener.onEventsFetched(eventList); // Send the fetched list to the listener
            } else {
                Log.w("FirebaseDB", "Failed to fetch events", task.getException());
                listener.onEventsFetchFailed(task.getException().getMessage());
            }
        });
    }

    // Update Event
    public void updateEvent(Event event) {
        databaseRef.child(event.getEvent_id()).setValue(event)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseDB", "Event updated successfully"))
                .addOnFailureListener(e -> Log.w("FirebaseDB", "Failed to update event", e));
    }

    // Delete Event
    public void deleteEvent(String eventId) {
        databaseRef.child(eventId).removeValue()
                .addOnSuccessListener(aVoid -> Log.d("FirebaseDB", "Event deleted successfully"))
                .addOnFailureListener(e -> Log.w("FirebaseDB", "Failed to delete event", e));
    }

    // Register for event
    public void registerForEvent(String eventId, String userId) {
        DatabaseReference registrationRef = FirebaseDatabase.getInstance().getReference("eventsRegistration");
        registrationRef.child(eventId).child(userId).setValue(true)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseDB", "User registered for event successfully"))
                .addOnFailureListener(e -> Log.w("FirebaseDB", "Failed to register for event", e));
    }

    // Bookmark Event
    public void bookmarkEvent(String eventId, String userId) {
        DatabaseReference bookmarkRef = FirebaseDatabase.getInstance().getReference("bookMarkedEvents");
        bookmarkRef.child(userId).child(eventId).setValue(true)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseDB", "Event bookmarked successfully"))
                .addOnFailureListener(e -> Log.w("FirebaseDB", "Failed to bookmark event", e));
    }
}
