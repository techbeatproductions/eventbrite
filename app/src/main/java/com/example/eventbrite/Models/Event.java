package com.example.eventbrite.Models;

public class Event {
    private String event_id;
    private String event_name;
    private String event_description;
    private String event_tag;
    private String event_date;
    private String event_time;
    private String event_location;
    private String event_image;
    private Double event_ticket_price;

    //Default constructor for Firebase
    public Event() {
    }

    public Event(String event_id, String event_name, String event_description, String event_tag, String event_date, String event_time, String event_location, String event_image, Double event_ticket_price) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_description = event_description;
        this.event_tag = event_tag;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_location = event_location;
        this.event_image = event_image;
        this.event_ticket_price = event_ticket_price;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_tag() {
        return event_tag;
    }

    public void setEvent_tag(String event_tag) {
        this.event_tag = event_tag;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    public Double getEvent_ticket_price() {
        return event_ticket_price;
    }

    public void setEvent_ticket_price(Double event_ticket_price) {
        this.event_ticket_price = event_ticket_price;
    }
}
