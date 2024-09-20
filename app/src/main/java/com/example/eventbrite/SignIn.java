package com.example.eventbrite;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

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
    public void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        //Sign In Success
                        Log.d(TAG, "Sign in with email: Success");

                        //Navigate to another activity
                    }else{
                        Log.w(TAG, "loginUser:Sign in with email failure ", task.getException());
                        Toast.makeText(SignIn.this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show();


                    }
                });

    }

    @Override
    public void onFragmentViewCreated(AuthenticationFrag fragment) {
        // Call fragment methods after views are initialized
        fragment.showViews();
        fragment.setStrings(R.string.sign_in, R.string.dont_have_an_account, R.string.sign_up, R.string.sign_in);
        // Log fragment state update
        Log.d(TAG, "Fragment views shown and strings set.");
    }
}