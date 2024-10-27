package com.example.wandersyncteam10.view;

// TravelLog class to represent individual travel log entries
/**
 * Represents an individual travel log entry, including the location,
 * start and end dates, and the duration of the travel.
 */
public class TravelLog {
    private String location;
    private String startDate;
    private String endDate;
    private int duration;

    /**
     * Default constructor needed for Firebase.
     */
    public TravelLog() {

    }

    /**
     * Constructor to create a TravelLog object with specified details.
     *
     * @param location The location of the travel.
     * @param startDate The start date of the travel.
     * @param endDate The end date of the travel.
     * @param duration The duration of the travel in days.
     */
    public TravelLog(String location, String startDate, String endDate, int duration) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    /**
     * Gets the location of the travel log entry.
     * @return The location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the travel log entry.
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the start date of the travel log entry.
     * @return The start date.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the travel log entry.
     * @param startDate The start date to set.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the travel log entry.
     * @return The end date.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the travel log entry.
     * @param endDate The end date to set.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the duration of the travel log entry.
     * @return The duration in days.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the travel log entry.
     * @param duration The duration to set, in days.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
