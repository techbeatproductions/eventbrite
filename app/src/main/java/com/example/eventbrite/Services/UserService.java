package com.example.eventbrite.Services;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.eventbrite.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.example.eventbrite.Services.UserProfileCallback;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private DatabaseReference getUserDatabaseReference(String userId){
        return FirebaseDatabase.getInstance().getReference("Users").child(userId);
    }

    // Method to fetch a user's profile from Firebase
    public void fetchUserProfile(String userId, final UserProfileCallback callback) {
        getUserDatabaseReference(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Map the snapshot to a User object
                    User user = dataSnapshot.getValue(User.class);
                    callback.onSuccess(user);  // Pass the user to the callback
                } else {
                    callback.onFailure("User not found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }
    //Method to update profile
    public void updateProfile(User user, String newName, String newEmail, String newAbout){
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newName);
        updates.put("email", newEmail);
        updates.put("about", newAbout);

        getUserDatabaseReference(user.getUserId()).updateChildren(updates);
    }

    // Add this method to UserService
    public void updateUserAbout(String userId, String newAbout) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("about", newAbout);
        getUserDatabaseReference(userId).updateChildren(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Successfully updated the about field
                    } else {
                        // Handle the error
                    }
                });
    }

    public void updateProfileImage(String userId, String imageUrl) {
        getUserDatabaseReference(userId).child("profileImage").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Profile image updated successfully for userId: " + userId);
                    } else {
                        Log.e(TAG, "Error updating profile image for userId: " + userId);
                    }
                });
    }





    //Method to follow another user
    public void followUser(User user, String targetUserId){
        //Add target user to the current user's 'following' list
        List<String> following = user.getFollowing();
        following.add(targetUserId);
        getUserDatabaseReference(user.getUserId()).child("following").setValue(following);

        // Update target user's followers list
        DatabaseReference targetUserRef = getUserDatabaseReference(targetUserId);
        targetUserRef.child("followers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> targetUserFollowers = (List<String>) task.getResult().getValue();
                if (targetUserFollowers == null) {
                    targetUserFollowers = new ArrayList<>();
                }
                if (!targetUserFollowers.contains(user.getUserId())) {
                    targetUserFollowers.add(user.getUserId());
                    targetUserRef.child("followers").setValue(targetUserFollowers);
                }
            }
        });

    }

    // Method to unfollow another user
    public void unfollowUser(User user, String targetUserId) {
        // Remove target user from the current user's 'following' list
        List<String> following = user.getFollowing();
        if (following.contains(targetUserId)) {
            following.remove(targetUserId);
            getUserDatabaseReference(user.getUserId()).child("following").setValue(following);
        }

        // Update target user's followers list
        DatabaseReference targetUserRef = getUserDatabaseReference(targetUserId);
        targetUserRef.child("followers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> targetUserFollowers = (List<String>) task.getResult().getValue();
                if (targetUserFollowers != null && targetUserFollowers.contains(user.getUserId())) {
                    targetUserFollowers.remove(user.getUserId());
                    targetUserRef.child("followers").setValue(targetUserFollowers);
                }
            }
        });
    }

    // Method to share an event
    public void shareEvent(User user, String eventId) {
        // Logic to share the event can be extended based on requirements
        System.out.println(user.getName() + " shared the event with ID " + eventId);
    }

    // Method to invite others to an event

    public void inviteOthersToEvent(User user, String eventId, List<String> userIds) {
        for (String inviteeId : userIds) {
            DatabaseReference inviteRef = FirebaseDatabase.getInstance().getReference("Users").child(inviteeId).child("invitations");
            inviteRef.push().setValue(eventId);
        }
        System.out.println("Users have been invited to the event with ID " + eventId);
    }

    // Method to create a user profile in Firebase Realtime Database
    public void createUserProfile(User user, final ProfileSaveCallback callback) {
        getUserDatabaseReference(user.getUserId()).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onSuccess(false);
                    }
                });
    }

    // Callback interface for saving the profile
    public interface ProfileSaveCallback {
        void onSuccess(boolean success);
    }

    public interface UserProfileCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }





}
