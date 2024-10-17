package com.example.wandersyncteam10.view;

public class TravelLog {
    private String location;
    private String startDate;
    private String endDate;

    public TravelLog(String location, String startDate, String endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }


}

