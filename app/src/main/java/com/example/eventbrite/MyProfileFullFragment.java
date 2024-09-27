package com.example.eventbrite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventbrite.Models.User;
import com.example.eventbrite.Services.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFullFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFullFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userId;
    private TextView userAboutText;
    private TextInputEditText aboutMeEditText;
    private UserService userService;
    private TextInputLayout aboutMeTextInputLayout;

    private Handler handler = new Handler();
    private Runnable updateRunnable;
    private final long typingDelay = 100000;

    public MyProfileFullFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFullFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFullFragment newInstance(String param1, String param2) {
        MyProfileFullFragment fragment = new MyProfileFullFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile_full, container, false);

        userAboutText = (TextView) view.findViewById(R.id.aboutMeFullTextTV);
        aboutMeEditText = view.findViewById(R.id.AboutMeTextInputEditText);
        aboutMeTextInputLayout = view.findViewById(R.id.aboutMeTextInputLayout); // Reference to the TextInputLayout
        Button editProfileButton = view.findViewById(R.id.EditProfileMyProfileFullFragmentBtn); // Get reference to the button

        // Retrieve the user ID from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);



        // Fetch the user profile if userId is available
        fetchUserProfile(userId);

        // Set up click listener for the edit profile button
        editProfileButton.setOnClickListener(v -> {
            // Enable the TextInputEditText for editing and make it visible
            aboutMeTextInputLayout.setVisibility(View.VISIBLE);
            aboutMeEditText.setEnabled(true);
            aboutMeEditText.requestFocus();

            // Show the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(aboutMeEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // Set up TextWatcher for the aboutMeEditText
        aboutMeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Remove any previous callbacks
                handler.removeCallbacks(updateRunnable);

                // Schedule a new runnable to update the database after a delay
                updateRunnable = () -> updateAboutMe(editable.toString());
                handler.postDelayed(updateRunnable, typingDelay);
            }
        });

        // Set up editor action listener for the Enter key
        aboutMeEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                // Update about me in the database
                updateAboutMe(aboutMeEditText.getText().toString());
                return true; // Indicate that we've handled the event
            }
            return false; // Not handled
        });

        return view;
    }

    private void fetchUserProfile(String userId) {
        if (userId != null) {

            userService.fetchUserProfile(userId, new UserService.UserProfileCallback() {
                @Override
                public void onSuccess(User user) {
                    // Update the UI with the user's information
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
        // Use the new method to update the about text in the user profile in the database
        userService.updateUserAbout(userId, newAbout);
        userAboutText.setText(newAbout); // Update the text view

        // Hide the TextInputLayout after the update
        aboutMeTextInputLayout.setVisibility(View.GONE);
    }

}