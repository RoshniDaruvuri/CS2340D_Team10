package com.example.wandersyncteam10.view;

/**
 * Represents a travel post containing details about a trip, including dates,
 * destination, accommodation, dining, transportation, and notes
 */
public class TravelPost {
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
     * Constructs a TravelPost instance with specified details
     * @param startDate      the start date of the trip
     * @param endDate        the end date of the trip
     * @param destination    the destination of the trip
     * @param accommodation  the accommodation details for the trip
     * @param dining         the dining details for the trip
     * @param transportation the transportation details for the trip
     * @param notes          additional notes about the trip
     */
    public TravelPost(String startDate, String endDate, String destination,
                      String accommodation, String dining, String transportation, String notes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.accommodation = accommodation;
        this.dining = dining;
        this.transportation = transportation;
        this.notes = notes;
    }

    /**
     * Gets the start date of the trip
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date of the trip
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Gets the destination of the trip
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Gets the accommodation details for the trip
     * @return the accommodation details
     */
    public String getAccommodation() {
        return accommodation;
    }

    /**
     * Gets the dining details for the trip
     * @return the dining details
     */
    public String getDining() {
        return dining;
    }

    /**
     * Gets the transportation details for the trip
     * @return the transportation details
     */
    public String getTransportation() {
        return transportation;
    }

    /**
     * Gets additional notes about the trip
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }
}
