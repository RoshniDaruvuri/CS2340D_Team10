//package com.example.wandersyncteam10.model;
package com.example.wandersyncteam10.view;

public class CommunityLog {
    private String communityType;
    private String startDate;
    private String endDate;
    private String startLocation;
    private String endLocation;

    public CommunityLog() {
        // Default constructor required for calls to DataSnapshot.getValue(CommunityLog.class)
    }

    public CommunityLog(String communityType, String startDate, String endDate, String startLocation, String endLocation) {
        this.communityType = communityType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    // Getters and setters
    public String getCommunityType() {
        return communityType;
    }

    public void setCommunityType(String communityType) {
        this.communityType = communityType;
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

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
}
