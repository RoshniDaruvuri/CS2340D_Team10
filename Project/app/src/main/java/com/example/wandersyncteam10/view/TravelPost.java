package com.example.wandersyncteam10.view;

/**
 * Represents a travel post containing details about a trip, including a common ID,
 * dates, destination, accommodation, dining, transportation, and notes.
 */
public class TravelPost {
    private String commonId; //the id to connect the community database and destination database
    private String startDate;
    private String endDate;
    private String destination;
    private String accommodation;
    private String dining;
    private String transportation;
    private String notes;

    /**
     * Default constructor required for Firebase
     */
    public TravelPost() {
        // Default constructor
    }

    /**
     * Constructs a TravelPost instance with specified details.
     *
     * @param commonId       a unique identifier for the post
     * @param startDate      the start date of the trip
     * @param endDate        the end date of the trip
     * @param destination    the destination of the trip
     * @param accommodation  the accommodation details for the trip
     * @param dining         the dining details for the trip
     * @param transportation the transportation details for the trip
     * @param notes          additional notes about the trip
     */
    public TravelPost(String commonId, String startDate, String endDate, String destination,
                      String accommodation, String dining, String transportation, String notes) {
        this.commonId = commonId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.accommodation = accommodation;
        this.dining = dining;
        this.transportation = transportation;
        this.notes = notes;
    }

    // Getters
    public String getCommonId() {
        return commonId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDestination() {
        return destination;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public String getDining() {
        return dining;
    }

    public String getTransportation() {
        return transportation;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setCommonId(String commonId) {
        this.commonId = commonId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public void setDining(String dining) {
        this.dining = dining;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
