
package com.example.wandersyncteam10.view;

public class TravelLog {
    private String location;
    private String startDate;
    private String endDate;
    private int duration;

    public TravelLog() {
        // Initialize fields with default values
        this.location = "";
        this.startDate = "";
        this.endDate = "";
        this.duration = 0;
    }

    public TravelLog(String location, String startDate, String endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;

    }

    // New 4-parameter constructor for including duration
    public TravelLog(String location, String startDate, String endDate, int duration) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }


    public String getLocation() {
        return location;
    }
    public void setLocation(String location) { this.location = location; }


    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) { this.startDate = startDate; }


    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}





