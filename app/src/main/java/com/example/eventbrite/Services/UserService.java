package com.example.eventbrite.Services;

import com.example.eventbrite.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;


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

    //Method to follow another user
    public void followUser(User user, String targetUserId){
        //Add target user to the 'following' list
        List<String> following = user.getFollowing();
        following.add(targetUserId);
        getUserDatabaseReference(user.getUserId()).child("following").setValue(following);

        //Update target user's followers list
        DatabaseReference targetUserRef = FirebaseDatabase.getInstance().getReference("Users").child(targetUserId);
        targetUserRef.child("followers").get().addOnCompleteListener(task -> {
            List<String> targetUserFollowers = (List<String>) task.getResult().getValue();
            if (targetUserFollowers != null) {
                targetUserFollowers.add(user.getUserId());
                targetUserRef.child("followers").setValue(targetUserFollowers);
            }
        });

    }

    // Method to unfollow another user
    public void unfollowUser(User user, String targetUserId) {
        // Remove target user from the 'following' list
        List<String> following = user.getFollowing();
        following.remove(targetUserId);
        getUserDatabaseReference(user.getUserId()).child("following").setValue(following);

        // Update target user's followers list
        DatabaseReference targetUserRef = FirebaseDatabase.getInstance().getReference("Users").child(targetUserId);
        targetUserRef.child("followers").get().addOnCompleteListener(task -> {
            List<String> targetUserFollowers = (List<String>) task.getResult().getValue();
            if (targetUserFollowers != null) {
                targetUserFollowers.remove(user.getUserId());
                targetUserRef.child("followers").setValue(targetUserFollowers);
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


}
