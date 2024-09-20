package com.example.eventbrite.Services;

import com.example.eventbrite.Models.User;

public interface UserProfileCallback {
    void onSuccess(User user);
    void onFailure(String errorMessage);
}
