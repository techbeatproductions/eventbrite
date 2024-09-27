package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventbrite.Adapters.NotificationAdapter;
import com.example.eventbrite.Models.Notification;
import com.example.eventbrite.Services.NotificationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView notificationItemRecyclerView;
    private NotificationAdapter notificationAdapter;
    private NotificationService notificationService;
    private List<Notification> notifications;

    private ImageView noNotificationsIV;
    private TextView noNotificationsTV;
    private ImageView backBtn;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notifications = new ArrayList<>();
        notificationService = new NotificationService();

        // Get current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser != null ? currentUser.getUid() : null;

        if (userId != null) {
            notificationService.fetchNotificationsForUser(userId, new NotificationService.OnNotificationsFetchedListener() {
                @Override
                public void onNotificationsFetched(List<Notification> fetchedNotifications) {
                    notifications.clear();
                    notifications.addAll(fetchedNotifications);
                    notificationAdapter.updateNotifications(notifications);
                    updateNoNotificationsView();
                }

                @Override
                public void onFetchFailed(String errorMessage) {
                    // Handle error, log it for debugging
                    Log.e("NotificationFragment", "Failed to fetch notifications: " + errorMessage);
                }
            });
        } else {
            Log.e("NotificationFragment", "User ID is null. User is not authenticated.");
            // Optionally show a message to the user or navigate to a login screen
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationItemRecyclerView = view.findViewById(R.id.notificationItemRecyclerView);
        noNotificationsIV = view.findViewById(R.id.noNotificationsIV);
        noNotificationsTV = view.findViewById(R.id.noNotificationsTV);
        backBtn = view.findViewById(R.id.backBtnInNotificationFragIV); // Initialize back button

        // Initialize the RecyclerView
        notificationAdapter = new NotificationAdapter(notifications);
        notificationItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationItemRecyclerView.setAdapter(notificationAdapter);

        // Set OnClickListener for the back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Home Activity
                Intent intent = new Intent(getActivity(), Home.class); // Change to your Home Activity class name
                startActivity(intent);
                getActivity().finish(); // Optional: finish current activity
            }
        });

        return view;
    }

    private void updateNoNotificationsView() {
        if (notifications.isEmpty()) {
            notificationItemRecyclerView.setVisibility(View.INVISIBLE);
            noNotificationsIV.setVisibility(View.VISIBLE);
            noNotificationsTV.setVisibility(View.VISIBLE);
        } else {
            notificationItemRecyclerView.setVisibility(View.VISIBLE);
            noNotificationsIV.setVisibility(View.INVISIBLE);
            noNotificationsTV.setVisibility(View.INVISIBLE);
        }
    }
}
