package com.example.wandersyncteam10.view;
public class TravelPost {
    private String startDate;
    private String endDate;
    private String destination;
    private String accommodation;
    private String dining;
    private String transportation;
    private String notes;

    public TravelPost() {
        // Default constructor required for Firebase
    }

    public TravelPost(String startDate, String endDate, String destination, String accommodation, String dining, String transportation, String notes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.accommodation = accommodation;
        this.dining = dining;
        this.transportation = transportation;
        this.notes = notes;
    }

    // Getters and setters for each field
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getDestination() { return destination; }
    public String getAccommodation() { return accommodation; }
    public String getDining() { return dining; }
    public String getTransportation() { return transportation; }
    public String getNotes() { return notes; }
}
