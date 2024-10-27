package com.example.wandersyncteam10.view;

/**
 * Represents a calculated duration between a start and end date.
 */
public class CalculatedDuration {
    private String startDate;
    private String endDate;
    private int duration;

    /**
     * Default constructor for Firebase.
     */
    public CalculatedDuration() {

    }

    /**
     * Constructs a CalculatedDuration with the specified start date, end date, and duration.
     * @param startDate The start date of the duration.
     * @param endDate   The end date of the duration.
     * @param duration   The duration in an integer format.
     */
    public CalculatedDuration(String startDate, String endDate, int duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    /**
     * Gets the start date of the duration.
     * @return The start date.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date of the duration.
     * @return The end date.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Gets the duration in an integer format.
     * @return The duration.
     */
    public int getDuration() {
        return duration;
    }
}
