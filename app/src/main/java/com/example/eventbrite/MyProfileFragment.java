package com.example.eventbrite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eventbrite.Models.User;
import com.example.eventbrite.Services.UserService;

public class MyProfileFragment extends Fragment {

    private static final String TAG = "MyProfileFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView fullNameMyProfileTV, followersTextView, followingTextView;
    private ImageView profilePhoto;
    private String userId;
    private User user; // Store user profile data temporarily
    private UserService userService;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Initialize UserService
        userService = new UserService();

        // Retrieve the user ID from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        Log.d(TAG, "Fetching user profile for user ID: " + userId);

        // Fetch user profile if the userId is available
        if (userId != null) {
            fetchUserProfile(userId);
        } else {
            Toast.makeText(getContext(), "User ID not found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        // Initialize the views
        fullNameMyProfileTV = view.findViewById(R.id.fullNameMyProfileTV);
        followingTextView = view.findViewById(R.id.followingNumberMyProfileTV);
        followersTextView = view.findViewById(R.id.followersNumberMyProfileTV);
        ImageView backBtnMyProfileFragIV = view.findViewById(R.id.backBtnMyProfileFragIV);
        profilePhoto = view.findViewById(R.id.profilePhotoMyProfileIV);

        // Open ProfilePhotoFragment when the profile photo is clicked
        profilePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllFragmentsActivity.class);
            intent.putExtra("fragmentToLoad", "ProfilePhoto");
            startActivity(intent);
        });

        // Handle back button
        backBtnMyProfileFragIV.setOnClickListener(v -> requireActivity().finish());

        // Load user data into the views
        if (user != null) {
            updateUI(user);
        }

        return view;
    }

    private void fetchUserProfile(String userId) {
        userService.fetchUserProfile(userId, new UserService.UserProfileCallback() {
            @Override
            public void onSuccess(User user) {
                MyProfileFragment.this.user = user; // Store user profile data
                if (getView() != null) { // Ensure the view is ready before updating UI
                    updateUI(user);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error fetching profile: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(User user) {
        fullNameMyProfileTV.setText(user.getName());
        followersTextView.setText(String.valueOf(user.getFollowers().size()));
        followingTextView.setText(String.valueOf(user.getFollowing().size()));

        // Load profile image from SharedPreferences if not available in the user object
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String profileImageUrl = sharedPreferences.getString("profileImage", "");

        // Check if the user has a profile image
        if (!profileImageUrl.isEmpty()) {
            // Load the profile image using Glide
            Glide.with(this) // Use Fragment's context
                    .load(profileImageUrl)
                    .skipMemoryCache(true) // Skip memory cache
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
                    .into(profilePhoto);
        } else {
            // Optionally, set a default profile image
            profilePhoto.setImageResource(R.drawable.sample_organizer_profile_photo);
        }
    }
}
