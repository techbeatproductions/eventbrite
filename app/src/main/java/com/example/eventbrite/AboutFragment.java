package com.example.eventbrite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.eventbrite.Models.User;
import com.example.eventbrite.Services.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AboutFragment extends Fragment {

    private TextView userAboutText;
    private TextInputEditText aboutMeEditText;
    private UserService userService;
    private TextInputLayout aboutMeTextInputLayout;
    private String userId;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserService();

        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        userAboutText = view.findViewById(R.id.aboutMeFullTextTV);
        aboutMeEditText = view.findViewById(R.id.AboutMeTextInputEditText);
        aboutMeTextInputLayout = view.findViewById(R.id.aboutMeTextInputLayout);
        Button editProfileButton = view.findViewById(R.id.editProfileButton);

        // Fetch user profile when the fragment is created
        fetchUserProfile(userId);

        // Set up click listener for the edit profile button
        editProfileButton.setOnClickListener(v -> {
            aboutMeTextInputLayout.setVisibility(View.VISIBLE);
            aboutMeEditText.setEnabled(true);
            aboutMeEditText.requestFocus();
        });

        // Set up TextWatcher for aboutMeEditText
        aboutMeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                updateAboutMe(editable.toString());
            }
        });

        return view;
    }

    private void fetchUserProfile(String userId) {
        if (userId != null) {
            userService.fetchUserProfile(userId, new UserService.UserProfileCallback() {
                @Override
                public void onSuccess(User user) {
                    userAboutText.setText(user.getAbout());
                    aboutMeEditText.setText(user.getAbout());
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getContext(), "Error fetching profile: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "User ID not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAboutMe(String newAbout) {
        userService.updateUserAbout(userId, newAbout);
        userAboutText.setText(newAbout); // Update the text view
    }
}
