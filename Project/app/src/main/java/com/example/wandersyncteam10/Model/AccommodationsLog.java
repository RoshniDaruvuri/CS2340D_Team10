package com.example.wandersyncteam10.Model;

/**
 * Represents an accommodation log entry with details such as check-in, check-out, location, room number, and room type.
 */
public class AccommodationsLog {
    private String checkin;
    private String checkout;
    private String location;
    private String roomnum;
    private String roomtype;

    /**
     * Default constructor for creating an empty AccommodationsLog instance.
     */
    public AccommodationsLog() {
    }

    /**
     * Parameterized constructor for creating an AccommodationsLog instance with specified values.
     *
     * @param checkin  the check-in date
     * @param checkout the check-out date
     * @param location the location of the accommodation
     * @param roomnum  the room number
     * @param roomtype the type of the room
     */
    public AccommodationsLog(String checkin, String checkout, String location, String roomnum, String roomtype) {
        this.checkin = checkin;
        this.checkout = checkout;
        this.location = location;
        this.roomnum = roomnum;
        this.roomtype = roomtype;
    }

    /**
     * Gets the check-in date
     * @return the check-in date
     */
    public String getCheckin() {
        return checkin;
    }

    /**
     * Sets the check-in date
     * @param checkin the check-in date
     */
    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    /**
     * Gets the check-out date
     * @return the check-out date
     */
    public String getCheckout() {
        return checkout;
    }

    /**
     * Sets the check-out date
     * @param checkout the check-out date
     */
    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    /**
     * Gets the location of the accommodation.
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the accommodations
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the room number
     * @return the room number
     */
    public String getRoomnum() {
        return roomnum;
    }

    /**
     * Sets the room number
     * @param roomnum the room number
     */
    public void setRoomnum(String roomnum) {
        this.roomnum = roomnum;
    }

    /**
     * Gets the room type
     * @return the room type
     */
    public String getRoomtype() {
        return roomtype;
    }

    /**
     * Sets the room type
     * @param roomtype the room type
     */
    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }
}
