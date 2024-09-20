package com.example.eventbrite;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
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

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();




    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);

        }
    }

    public void createUser (String email, String password, String fullName){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        //Update profile with full name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build();

                        if (mUser != null){
                            mUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if(profileTask.isSuccessful()){
                                            Log.d("User Name Saving", "Full User Name Saved Correctly");
                                        }
                                    });
                        }

                    } else {
                        String errorMessage = "Registration Failed";

                        // Check if task exception is not null and parse message
                        if (task.getException() != null) {
                            String fullErrorMessage = task.getException().getMessage();

                            // Extract the part inside square brackets []
                            int start = fullErrorMessage.indexOf("[");
                            int end = fullErrorMessage.indexOf("]");

                            if (start != -1 && end != -1) {
                                errorMessage = fullErrorMessage.substring(start + 1, end); // Get text inside []
                            } else {
                                errorMessage = fullErrorMessage; // Fallback to full message if brackets are not found
                            }
                        }

                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUp.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });

    }
}