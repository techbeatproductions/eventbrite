package com.example.eventbrite.Models;

import java.io.Serializable;

public class Notification implements Serializable {
    private String id;          // Unique ID for the notification
    private String recipientId; // The ID of the user who receives the notification
    private String actorId;     // The ID of the user who performed the action (follow, unfollow, invite)
    private String message;     // The notification message
    private String type;        // The type of notification (e.g., "follow", "invite")
    private String actorName;    // The name of the user who performed the action
    private String eventName;     // The name of the event (if applicable)
    private long timestamp;     // Time when the notification was created



    public Notification(String id, String recipientId, String actorId, String message, String type, String actorName, String eventName, long timestamp) {
        this.id = id;
        this.recipientId = recipientId;
        this.actorId = actorId;
        this.message = message;
        this.type = type;
        this.actorName = actorName;
        this.eventName = eventName;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
