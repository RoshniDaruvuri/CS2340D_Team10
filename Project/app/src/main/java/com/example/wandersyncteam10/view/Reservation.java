package com.example.wandersyncteam10.view;

public class Reservation {
    private String location;
    private String website;
    private String time;

    public Reservation(String location, String website, String time) {
        this.location = location;
        this.website = website;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getTime() {
        return time;
    }


    public boolean isValid() {
        return !(location.isEmpty() || website.isEmpty() || time.isEmpty());
    }
}

