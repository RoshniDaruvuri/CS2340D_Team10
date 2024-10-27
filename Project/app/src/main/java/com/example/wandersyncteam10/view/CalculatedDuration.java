package com.example.wandersyncteam10.view;

public class CalculatedDuration {
    private String startDate;
    private String endDate;
    private int duration;

    // Default constructor for Firebase
    public CalculatedDuration() {}

    public CalculatedDuration(String startDate, String endDate, int duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    // Getters and setters (optional, depending on your usage)
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public int getDuration() { return duration; }
}
