package com.example.wandersyncteam10.Model;

/**
 * Represents a reservation with details such as location, website, and time.
 */
public class Reservation {
    private String location;
    private String website;
    private String time;

    /**
     * Constructs a Reservation instance with specified location, website, and time
     * @param location the location of the reservation
     * @param website  the website for the reservation
     * @param time     the time of the reservation
     */
    public Reservation(String location, String website, String time) {
        this.location = location;
        this.website = website;
        this.time = time;
    }

    /**
     * Gets the location of the reservation
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the website associated with the reservation
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Gets the time of the reservation
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Checks if the reservation details are valid.
     * A reservation is considered valid if none of the fields are empty.
     * @return {@code true} if the reservation details are valid, {@code false} otherwise
     */
    public boolean isValid() {
        return !(location.isEmpty() || website.isEmpty() || time.isEmpty());
    }
}
