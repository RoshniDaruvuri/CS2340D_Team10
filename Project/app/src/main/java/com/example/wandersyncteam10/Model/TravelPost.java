package com.example.wandersyncteam10.Model;

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
    /**
     * Retrieves the common ID of the travel post.
     *
     * @return the common ID
     */
    public String getCommonId() {
        return commonId;
    }

    /**
     * Retrieves the start date of the trip.
     *
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Retrieves the end date of the trip.
     *
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Retrieves the destination of the trip.
     *
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Retrieves the accommodation details of the trip.
     *
     * @return the accommodation details
     */
    public String getAccommodation() {
        return accommodation;
    }

    /**
     * Retrieves the dining details of the trip.
     *
     * @return the dining details
     */
    public String getDining() {
        return dining;
    }

    /**
     * Retrieves the transportation details of the trip.
     *
     * @return the transportation details
     */
    public String getTransportation() {
        return transportation;
    }

    /**
     * Retrieves additional notes about the trip.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    // Setters
    /**
     * Sets the common ID of the travel post.
     *
     * @param commonId the common ID to set
     */
    public void setCommonId(String commonId) {
        this.commonId = commonId;
    }

    /**
     * Sets the start date of the trip.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Sets the end date of the trip.
     *
     * @param endDate the end date to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Sets the destination of the trip.
     *
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Sets the accommodation details of the trip.
     *
     * @param accommodation the accommodation details to set
     */
    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    /**
     * Sets the dining details of the trip.
     *
     * @param dining the dining details to set
     */
    public void setDining(String dining) {
        this.dining = dining;
    }

    /**
     * Sets the transportation details of the trip.
     *
     * @param transportation the transportation details to set
     */
    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    /**
     * Sets additional notes about the trip.
     *
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
