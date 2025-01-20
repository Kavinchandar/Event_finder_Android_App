package com.example.hw9;

public class EventModel {

    private String event_id;
    private String event_image;
    private String event_name;

    private String event_date;

    private String event_time;

    private String event_genre;

    private String event_venue;


    // Constructor
    public EventModel(String event_id,String event_name, String event_image, String event_date, String event_time, String event_genre, String event_venue) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_image = event_image;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_genre = event_genre;
        this.event_venue = event_venue;
    }

    public static Object get(int position) {
        return position;
    }

    // Getter and Setter - Event ID
    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    // Getter and Setter - Event Name
    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

//     Getter and Setter - Event Icon
    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    // Getter and Setter - Event Date
    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    // Getter and Setter - Event Time
    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    // Getter and Setter - Event Genre
    public String getEvent_genre() {
        return event_genre;
    }

    public void setEvent_genre(String event_genre) {
        this.event_genre = event_genre;
    }

    // Getter and Setter - Event Venue
    public String getEvent_venue() {
        return event_venue;
    }

    public void setEvent_venue(String event_venue) {
        this.event_venue = event_venue;
    }


}
