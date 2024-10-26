package com.example.wandersyncteam10.view;

// TravelLog class to represent individual travel log entries
public class TravelLog {
    private String location;
    private String startDate;
    private String endDate;
    private int duration;

    // Default constructor needed for Firebase
    public TravelLog() {}

    // Constructor to create a TravelLog object
    public TravelLog(String location, String startDate, String endDate, int duration) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    // Getters and setters for accessing and modifying private fields
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

