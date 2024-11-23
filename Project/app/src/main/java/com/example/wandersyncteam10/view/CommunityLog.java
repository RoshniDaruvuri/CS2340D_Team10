package com.example.wandersyncteam10.view;

/**
 * Represents a log for a community activity, including details such as community type, dates, and locations.
 */
public class CommunityLog {
    private String communityType;
    private String startDate;
    private String endDate;
    private String startLocation;
    private String endLocation;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(CommunityLog.class).
     */
    public CommunityLog() {
        // Default constructor
    }

    /**
     * Constructs a CommunityLog instance with specified details.
     * @param communityType the type of community activity
     * @param startDate     the start date
     * @param endDate       the end date
     * @param startLocation the starting location
     * @param endLocation   the ending location
     */
    public CommunityLog(String communityType, String startDate,
                        String endDate, String startLocation, String endLocation) {
        this.communityType = communityType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    /**
     * Gets the type of the community activity
     * @return the community type
     */
    public String getCommunityType() {
        return communityType;
    }

    /**
     * Sets the type of the community activity
     * @param communityType the community type
     */
    public void setCommunityType(String communityType) {
        this.communityType = communityType;
    }

    /**
     * Gets the start date of the community activity
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the community activity
     * @param startDate the start date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the community activity
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the community activity
     * @param endDate the end date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the starting location of the community activity
     * @return the starting location
     */
    public String getStartLocation() {
        return startLocation;
    }

    /**
     * Sets the starting location of the community activity
     * @param startLocation the starting location
     */
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * Gets the ending location of the community activity
     * @return the ending location
     */
    public String getEndLocation() {
        return endLocation;
    }

    /**
     * Sets the ending location of the community activity
     * @param endLocation the ending location
     */
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
}
