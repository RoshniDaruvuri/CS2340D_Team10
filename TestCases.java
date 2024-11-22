package com.example.wandersyncteam10;

import com.example.wandersyncteam10.view.AccommodationsActivity;
import com.example.wandersyncteam10.view.AccommodationsDatabase;
import com.example.wandersyncteam10.view.AccommodationsLog;
import com.example.wandersyncteam10.view.DiningActivity;
import com.example.wandersyncteam10.view.Reservation;
import com.example.wandersyncteam10.view.TravelLog;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TestCases {

    @Test
    public void testValidDateParsing() {
        // Test a valid date string and check if it parses without exception
        String validDate = "2024-12-31";
        LocalDate parsedDate = LocalDate.parse(validDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertEquals("Parsed date should match expected", LocalDate.of(2024, 12, 31), parsedDate);
    }

    // Test: Calculate Total Vacation Days without Firebase
    @Test
    public void testTotalVacationDays() {
        // Create a list to store travel logs
        List<TravelLog> logs = new ArrayList<>();

        // Add sample travel logs
        logs.add(new TravelLog("Paris", "2024-01-01", "2024-01-07", 6));  // 6 days
        logs.add(new TravelLog("Tokyo", "2024-02-15", "2024-02-20", 5));  // 5 days

        // Calculate total vacation days
        int totalDays = 0;
        for (TravelLog log : logs) {
            totalDays += log.getDuration();
        }

        // Validate the result
        assertEquals("Total vacation days should be 11", 11, totalDays);
    }

    // Test 3: Validate Date Comparison Utility
    @Test
    public void testDateComparison() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 7);
        assertTrue("End date should be after start date", endDate.isAfter(startDate));
    }

    // Test 4: Validate Reversed Date Comparison
    @Test
    public void testInvalidDateComparison() {
        LocalDate startDate = LocalDate.of(2024, 1, 7);
        LocalDate endDate = LocalDate.of(2024, 1, 1);
        assertFalse("End date should not be before start date", endDate.isAfter(startDate));
    }

    // Test 5: Add Travel Logs to List
    @Test
    public void testAddTravelLogsToList() {
        List<TravelLog> logs = new ArrayList<>();
        // Calculate the duration manually
        logs.add(new TravelLog("Paris", "2024-01-01", "2024-01-07", 6));
        logs.add(new TravelLog("Tokyo", "2024-02-15", "2024-02-20", 5));

        assertEquals("There should be 2 travel logs", 2, logs.size());
    }

    // Test 6: Verify TravelLog Location Storage
    @Test
    public void testLocationStorage() {
        TravelLog log = new TravelLog("New York", "2024-03-01", "2024-03-10", 9);
        assertEquals("Location should be 'New York'", "New York", log.getLocation());
    }

    // Test 7: Calculate Duration of a Valid Trip
    @Test
    public void testTripDurationCalculation() {
        TravelLog log = new TravelLog("Paris", "2024-01-01", "2024-01-07", 6);
        assertEquals("Duration should be 6 days", 6, log.getDuration());
    }

    // Test 8: Handle Zero-Day Trip Duration
    @Test
    public void testZeroDuration() {
        TravelLog log = new TravelLog("Paris", "2024-01-01", "2024-01-01", 0);
        assertEquals("Duration should be 0 days", 0, log.getDuration());
    }

    // Test 9: Validate Utility to Format Trip Info
    @Test
    public void testTripInfoDisplay() {
        TravelLog log = new TravelLog("Tokyo", "2024-02-15", "2024-02-20", 5);
        String info = log.getLocation() + " (" + log.getDuration() + " days)";
        assertEquals("Trip info display should match", "Tokyo (5 days)", info);
    }

    // Test 10: Filter Travel Logs by Duration
    @Test
    public void testFilterLogsByDuration() {
        List<TravelLog> logs = new ArrayList<>();
        logs.add(new TravelLog("Paris", "2024-01-01", "2024-01-07", 6));
        logs.add(new TravelLog("Tokyo", "2024-02-15", "2024-02-16", 1)); // 1-day trip

        List<TravelLog> longTrips = logs.stream()
                .filter(log -> log.getDuration() > 3)
                .toList();

        assertEquals("There should be 1 long trip", 1, longTrips.size());
    }


    //new test cases
    @Test
    public void testAccommodationsLogConstructorAndGetters() {
        AccommodationsLog log = new AccommodationsLog("2024-11-10", "2024-11-12", "nyc", "1", "Deluxe");

        assertEquals("2024-11-10", log.getCheckin());
        assertEquals("2024-11-12", log.getCheckout());
        assertEquals("nyc", log.getLocation());
        assertEquals("1", log.getRoomnum());
        assertEquals("Deluxe", log.getRoomtype());
    }

    @Test
    public void testSetters() {
        AccommodationsLog log = new AccommodationsLog();
        log.setCheckin("2024-11-10");
        log.setCheckout("2024-11-12");
        log.setLocation("nyc");
        log.setRoomnum("1");
        log.setRoomtype("Deluxe");

        assertEquals("2024-11-10", log.getCheckin());
        assertEquals("2024-11-12", log.getCheckout());
        assertEquals("nyc", log.getLocation());
        assertEquals("1", log.getRoomnum());
        assertEquals("Deluxe", log.getRoomtype());
    }


    @Test(expected = java.time.format.DateTimeParseException.class)
    public void testInvalidDateParsing() {
        String invalidDate = "2024-31-12";
        LocalDate.parse(invalidDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Test
    public void testRequiredFields() {
        AccommodationsLog logWithoutLocation = new AccommodationsLog("2024-11-01", "2024-11-05", "", "1", "Standard");
        assertTrue("location should not be empty", logWithoutLocation.getLocation().isEmpty());

        AccommodationsLog logWithoutCheckin = new AccommodationsLog("", "2024-11-05", "hotel", "1", "Standard");
        assertTrue("check in date should not be empty", logWithoutCheckin.getCheckin().isEmpty());

        AccommodationsLog validLog = new AccommodationsLog("2024-11-01", "2024-11-05", "hotel", "1", "Suite");
        assertFalse("entry should not have empty fields", validLog.getCheckin().isEmpty() || validLog.getLocation().isEmpty());
    }

    @Test
    public void testValidReservation() {
        Reservation reservation = new Reservation("nyc", "www.nycdiner.com", "2024-11-15 10:00");

        assertTrue("reservation should be valid when all of the fields are filled", reservation.isValid());
    }

    @Test
    public void testInvalidReservationWithEmptyLocation() {
        Reservation reservation = new Reservation("", "www.nycdiner.com", "2024-11-15 10:00");

        assertFalse("reservation should be invalid when location is empty.", reservation.isValid());
    }

    @Test
    public void testInvalidReservationWithEmptyWebsite() {
        Reservation reservation = new Reservation("nyc", "", "2024-11-15 10:00");

        assertFalse("reservation should be invalid when the website is empty.", reservation.isValid());
    }

    @Test
    public void testInvalidReservationWithEmptyTime() {
        Reservation reservation = new Reservation("nyc", "www.nycdiner.com", "");

        assertFalse("reservation should be invalid when the time is empty", reservation.isValid());
    }

    @Test
    public void testInvalidReservationWithAllEmptyFields() {
        Reservation reservation = new Reservation("", "", "");

        assertFalse("reservation should be invalid when all fields are empty", reservation.isValid());
    }

    @Test
    public void testValidReservationWithSpecialCharacters() {
        Reservation reservation = new Reservation("nyc - Grand Plaza", "www.nycdiner.com", "2024-11-15 10:00");

        assertTrue("reservation should be valid when the fields contain special characters.", reservation.isValid());
    }

    @Test
    public void testReservationConstructor() {
        String location = "Paris";
        String website = "www.pariseats.com";
        String time = "2024-11-10 11:00";

        Reservation reservation = new Reservation(location, website, time);

        assertEquals("Paris", reservation.getLocation());
        assertEquals("www.pariseats.com", reservation.getWebsite());
        assertEquals("2024-11-10 11:00", reservation.getTime());
    }

    @Test
    public void testReservationFieldsNotEmpty() {
        // Given empty data
        String location = "";
        String website = "";
        String time = "";

        Reservation reservation = new Reservation(location, website, time);

        assertNotNull(reservation.getLocation());
        assertNotNull(reservation.getWebsite());
        assertNotNull(reservation.getTime());
    }

}
