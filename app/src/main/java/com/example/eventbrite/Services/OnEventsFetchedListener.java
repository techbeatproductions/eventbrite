package com.example.eventbrite.Services;

import com.example.eventbrite.Models.Event;

import java.util.List;

public interface OnEventsFetchedListener {
    void onEventsFetched(List<Event> events);
    void onEventsFetchFailed(String errorMessage);
}
