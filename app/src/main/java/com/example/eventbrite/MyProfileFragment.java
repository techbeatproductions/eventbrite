package com.example.eventbrite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Import the Log class

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventbrite.Models.User;
import com.example.eventbrite.Services.UserService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    private static final String TAG = "MyProfileFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView fullNameMyProfileTV, followersTextView, followingTextView;
    private String userId;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        fullNameMyProfileTV = view.findViewById(R.id.fullNameMyProfileTV);
        followersTextView = view.findViewById(R.id.followingNumberMyProfileTV);
        followingTextView = view.findViewById(R.id.followersNumberMyProfileTV);
        ImageView backBtnMyProfileFragIV = view.findViewById(R.id.backBtnMyProfileFragIV);

        // Retrieve the user ID from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        // Log the user ID
        Log.d(TAG, "Fetching user profile for user ID: " + userId);

        // Only fetch the user profile if the userId is not null
        if (userId != null) {
            fetchUserProfile(userId);
        } else {
            Toast.makeText(getContext(), "User ID not found.", Toast.LENGTH_SHORT).show();
        }

        // Set up back button listener
        backBtnMyProfileFragIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish(); // End current activity to go back
            }
        });

        return view;
    }

    private void fetchUserProfile(String userId) {
        UserService userService = new UserService();
        userService.fetchUserProfile(userId, new UserService.UserProfileCallback() {
            @Override
            public void onSuccess(User user) {
                // Log the retrieved user details
                Log.d(TAG, "User fetched: " + user.toString());

                // Update the UI with the user's information
                fullNameMyProfileTV.setText(user.getName());
                followersTextView.setText(String.valueOf(user.getFollowers().size()));
                followingTextView.setText(String.valueOf(user.getFollowing().size()));

                // Show a success toast message
                Toast.makeText(getContext(), "Profile loaded successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Show error toast if fetching the profile fails
                Toast.makeText(getContext(), "Error fetching profile: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
