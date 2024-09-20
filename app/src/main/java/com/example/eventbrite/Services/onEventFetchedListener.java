package com.example.eventbrite.Services;

import com.example.eventbrite.Models.Event;

public interface onEventFetchedListener {
    void onEventFetched(Event event);
    void onEventFetchFailed(String errorMessage);
}

