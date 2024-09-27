package com.example.eventbrite.Services;

import android.util.Log;

import com.example.eventbrite.Models.Notification;
import com.example.eventbrite.Utils.TimeUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final DatabaseReference notificationsRef;

    public NotificationService() {
        notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications");
    }

    // Create a new notification
    public void createNotification(String recipientId, String actorId, String actorName, String type, String eventName) {
        String notificationId = notificationsRef.push().getKey(); // Generate unique ID
        long timestamp = System.currentTimeMillis();
        Notification notification;

        switch (type) {
            case "follow":
                notification = new Notification(notificationId, recipientId, actorId, actorName + " followed you", type, actorName, null, timestamp);
                break;
            case "unfollow":
                notification = new Notification(notificationId, recipientId, actorId, actorName + " unfollowed you", type, actorName, null, timestamp);
                break;
            case "invite":
                notification = new Notification(notificationId, recipientId, actorId, actorName + " invited you to this event: " + eventName, type, actorName, eventName, timestamp);
                break;
            default:
                return; // Invalid type, handle as needed
        }

        notificationsRef.child(notificationId).setValue(notification)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Notification created successfully
                    } else {
                        // Handle error
                    }
                });
    }


    // Fetch notifications for a specific user
    public void fetchNotificationsForUser(String userId, OnNotificationsFetchedListener listener) {
        Log.d("NotificationService", "Fetching notifications for user: " + userId); // Log user ID
        notificationsRef.orderByChild("recipientId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Notification> notifications = new ArrayList<>();
                        Log.d("NotificationService", "Number of notifications found: " + dataSnapshot.getChildrenCount()); // Log count of notifications

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Notification notification = snapshot.getValue(Notification.class);
                            if (notification != null) {
                                notifications.add(notification);
                                Log.d("NotificationService", "Notification fetched: " + notification.getMessage()); // Log each notification message
                            } else {
                                Log.e("NotificationService", "Received null notification from database");
                            }
                        }
                        listener.onNotificationsFetched(notifications);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("NotificationService", "Failed to fetch notifications: " + databaseError.getMessage()); // Log error message
                        listener.onFetchFailed(databaseError.getMessage());
                    }
                });
    }

    // Interface for fetching notifications
    public interface OnNotificationsFetchedListener {
        void onNotificationsFetched(List<Notification> notifications);
        void onFetchFailed(String errorMessage);
    }
}
