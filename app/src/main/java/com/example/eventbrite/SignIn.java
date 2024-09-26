package com.example.eventbrite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventbrite.Models.User;
import com.example.eventbrite.Services.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignIn extends AppCompatActivity implements AuthenticationFrag.AuthenticationFragListener{

    private FirebaseAuth mAuth;
    private  static final  String TAG = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        View rootView = findViewById(R.id.main); // Ensure 'main' is the root view
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set full-screen mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        AuthenticationFrag fragment;
        fragment = (AuthenticationFrag) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView3);

        if (fragment == null) {
            // Initialize the fragment if it's not already in the container
            Log.d(TAG, "Fragment is null, initializing and replacing fragment.");
            fragment = new AuthenticationFrag();
            fragment.setListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView3, fragment)
                    .commit();
        }else {
            // Log when fragment is found
            Log.d(TAG, "Fragment found in the container.");
            fragment.setListener(this);
        }

        mAuth = FirebaseAuth.getInstance();


    }



    @Override
    public void onFragmentViewCreated(AuthenticationFrag fragment) {
        // Call fragment methods after views are initialized
        fragment.showViews();
        fragment.setStrings(R.string.sign_in, R.string.dont_have_an_account, R.string.sign_up, R.string.sign_in);
        // Log fragment state update
        Log.d(TAG, "Fragment views shown and strings set.");
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Sign in with email: Success");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", userId);
                            editor.apply(); // Save userId

                            // Fetch the user's profile from Firebase
                            UserService userService = new UserService();
                            userService.fetchUserProfile(userId, new UserService.UserProfileCallback() {
                                @Override
                                public void onSuccess(User user) {
                                    Log.d(TAG, "User profile fetched: " + user.getName());

                                    // Log the user type
                                    Log.d(TAG, "User type: " + user.getUserType());

                                    // Redirect to the appropriate activity based on userType
                                    Intent intent;
                                    if ("organizer".equals(user.getUserType())) {
                                        intent = new Intent(SignIn.this, CreateEvent.class);
                                    } else {
                                        intent = new Intent(SignIn.this, Home.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Log.w(TAG, "Failed to fetch user profile: " + errorMessage);
                                    // Optionally handle the failure by showing a message or redirecting to a default screen
                                    Toast.makeText(SignIn.this, "Failed to fetch user profile", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        // Handle sign-in failure
                        Exception exception = task.getException();
                        String errorMessage = exception != null ? exception.getMessage() : "Authentication failed";
                        Toast.makeText(SignIn.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }




}