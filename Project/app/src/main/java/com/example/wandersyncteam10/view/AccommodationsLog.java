package com.example.wandersyncteam10.view;

public class AccommodationsLog {
    private String checkin;
    private String checkout;
    private String location;
    private String roomnum;
    private String roomtype;

    // Default constructor
    public AccommodationsLog() {
    }

    // Parameterized constructor
    public AccommodationsLog(String checkin, String checkout, String location, String roomnum, String roomtype) {
        this.checkin = checkin;
        this.checkout = checkout;
        this.location = location;
        this.roomnum = roomnum;
        this.roomtype = roomtype;
    }

    // Getters and setters
    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(String roomnum) {
        this.roomnum = roomnum;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }
}
