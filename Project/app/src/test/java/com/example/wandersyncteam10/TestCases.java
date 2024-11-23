package com.example.wandersyncteam10;

import com.example.wandersyncteam10.view.AccommodationsActivity;
import com.example.wandersyncteam10.view.AccommodationsDatabase;
import com.example.wandersyncteam10.view.AccommodationsLog;
import com.example.wandersyncteam10.view.DiningActivity;
import com.example.wandersyncteam10.view.Reservation;
import com.example.wandersyncteam10.view.TravelLog;
import com.example.wandersyncteam10.view.TravelPost;
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

    @Test
    public void testTravelPostDisplay() {
        // Arrange
        TravelPost post = new TravelPost(
                "2024-11-01",
                "2024-11-05",
                "Paris",
                "Hotel Luxe",
                "Bistro",
                "Flight",
                "Exploring the city of lights!"
        );

        // Act
        String displayInfo = post.getDestination() + ": "
                + post.getStartDate() + " - "
                + post.getEndDate() + ", "
                + post.getNotes();

        // Assert
        assertEquals("Travel post display should match",
                "Paris: 2024-11-01 - 2024-11-05, Exploring the city of lights!",
                displayInfo);
    }

    @Test
    public void testDefaultConstructor() {
        // Arrange
        TravelPost post = new TravelPost();

        // Assert
        assertNull("Start date should be null", post.getStartDate());
        assertNull("End date should be null", post.getEndDate());
        assertNull("Destination should be null", post.getDestination());
        assertNull("Accommodation should be null", post.getAccommodation());
        assertNull("Dining should be null", post.getDining());
        assertNull("Transportation should be null", post.getTransportation());
        assertNull("Notes should be null", post.getNotes());
    }

    @Test
    public void testTravelDuration() {
        // Arrange
        TravelPost post = new TravelPost("2024-12-01", "2024-12-10", "Italy", "Villa", "Pasta House", "Car", "Exploring Tuscany");

        // Act
        String durationInfo = post.getDestination() + " ("
                + (Integer.parseInt(post.getEndDate().substring(8))
                - Integer.parseInt(post.getStartDate().substring(8))
                + 1) + " days)";

        // Assert
        assertEquals("Travel duration info should match", "Italy (10 days)", durationInfo);
    }

    @Test
    public void testPartialTravelPostInfo() {
        // Arrange
        TravelPost post = new TravelPost("2024-11-15", "2024-11-20", "Tokyo", "Ryokan", "Sushi Bar", "Train", null);

        // Act
        String partialInfo = post.getDestination() + ": "
                + post.getStartDate() + " - " + post.getEndDate() + "\n"
                + "Accommodation: " + post.getAccommodation() + "\n"
                + "Dining: " + post.getDining();

        // Assert
        assertEquals("Partial travel post info should match",
                "Tokyo: 2024-11-15 - 2024-11-20\n"
                        + "Accommodation: Ryokan\n"
                        + "Dining: Sushi Bar",
                partialInfo);
    }

    @Test
    public void testEmptyFields() {
        // Arrange
        TravelPost post = new TravelPost("", "", "", "", "", "", "");

        // Assert
        assertTrue("Start date should be empty", post.getStartDate().isEmpty());
        assertTrue("End date should be empty", post.getEndDate().isEmpty());
        assertTrue("Destination should be empty", post.getDestination().isEmpty());
        assertTrue("Accommodation should be empty", post.getAccommodation().isEmpty());
        assertTrue("Dining should be empty", post.getDining().isEmpty());
        assertTrue("Transportation should be empty", post.getTransportation().isEmpty());
        assertTrue("Notes should be empty", post.getNotes().isEmpty());
    }

    @Test
    public void testSingleDayTrip() {
        // Arrange
        TravelPost post = new TravelPost("2024-11-01", "2024-11-01", "Berlin", "Hostel", "Currywurst Stand", "Bike", "Visiting museums.");

        // Act
        String singleDayInfo = post.getDestination() + ": "
                + post.getStartDate() + " (1 day)";

        // Assert
        assertEquals("Single-day trip info should match", "Berlin: 2024-11-01 (1 day)", singleDayInfo);
    }

    @Test
    public void testFullTravelPostDetails() {
        // Arrange
        TravelPost post = new TravelPost(
                "2024-12-10",
                "2024-12-20",
                "Hawaii",
                "Beach Resort",
                "Seafood Grill",
                "Flight",
                "Relaxing on the beach and snorkeling."
        );

        // Act
        String fullDetails = "Destination: " + post.getDestination() + "\n"
                + "Dates: " + post.getStartDate() + " to " + post.getEndDate() + "\n"
                + "Accommodation: " + post.getAccommodation() + "\n"
                + "Dining: " + post.getDining() + "\n"
                + "Transportation: " + post.getTransportation() + "\n"
                + "Notes: " + post.getNotes();

        // Assert
        assertEquals("Full travel post details should match",
                "Destination: Hawaii\n"
                        + "Dates: 2024-12-10 to 2024-12-20\n"
                        + "Accommodation: Beach Resort\n"
                        + "Dining: Seafood Grill\n"
                        + "Transportation: Flight\n"
                        + "Notes: Relaxing on the beach and snorkeling.",
                fullDetails);
    }

    @Test
    public void testNullFields() {
        // Arrange
        TravelPost post = new TravelPost("2024-10-01", "2024-10-10", "Paris", null, null, "Train", null);

        // Act
        String details = "Destination: " + post.getDestination() + "\n"
                + "Dates: " + post.getStartDate() + " to " + post.getEndDate() + "\n"
                + "Accommodation: " + (post.getAccommodation() == null ? "N/A" : post.getAccommodation()) + "\n"
                + "Dining: " + (post.getDining() == null ? "N/A" : post.getDining()) + "\n"
                + "Transportation: " + post.getTransportation();

        // Assert
        assertEquals("Details with null fields should handle N/A gracefully",
                "Destination: Paris\n"
                        + "Dates: 2024-10-01 to 2024-10-10\n"
                        + "Accommodation: N/A\n"
                        + "Dining: N/A\n"
                        + "Transportation: Train",
                details);
    }

    @Test
    public void testLongNotes() {
        // Arrange
        String longNotes = "Visited multiple landmarks: Eiffel Tower, Louvre, Montmartre, and took a boat tour along the Seine River.";
        TravelPost post = new TravelPost("2024-08-01", "2024-08-05", "Paris", "Hotel", "French Bistro", "Metro", longNotes);

        // Act
        String extractedNotes = post.getNotes();

        // Assert
        assertEquals("Notes should match the provided long text",
                "Visited multiple landmarks: Eiffel Tower, Louvre, Montmartre, and took a boat tour along the Seine River.",
                extractedNotes);
    }

    @Test
    public void testMultipleTravelPosts() {
        // Arrange
        TravelPost post1 = new TravelPost("2024-07-10", "2024-07-15", "Rome", "Hotel", "Pizza Place", "Bus", "Exploring the Colosseum.");
        TravelPost post2 = new TravelPost("2024-08-10", "2024-08-20", "Barcelona", "Hostel", "Tapas Bar", "Train", "Enjoying the beach and Gaudí's architecture.");

        // Act & Assert
        assertEquals("First travel post destination should be Rome", "Rome", post1.getDestination());
        assertEquals("Second travel post destination should be Barcelona", "Barcelona", post2.getDestination());
    }

    @Test
    public void testTransportationField() {
        // Arrange
        TravelPost post = new TravelPost("2024-06-15", "2024-06-20", "Sydney", "Apartment", "Seafood Market", "Ferry", "Enjoying the harbor.");

        // Act
        String transportation = post.getTransportation();

        // Assert
        assertEquals("Transportation should match the provided value", "Ferry", transportation);
    }

    @Test
    public void testTravelSummaryConcatenation() {
        // Arrange
        TravelPost post = new TravelPost("2024-03-01", "2024-03-05", "Berlin", "Airbnb", "Local Cafe", "Bike", "Visiting historical sites.");

        // Act
        String travelSummary = "Traveling to " + post.getDestination() + " from " + post.getStartDate() + " to " + post.getEndDate()
                + ". Staying at " + post.getAccommodation() + ", dining at " + post.getDining()
                + ", and getting around by " + post.getTransportation() + ".";

        // Assert
        assertEquals("Travel summary should concatenate all details correctly",
                "Traveling to Berlin from 2024-03-01 to 2024-03-05. Staying at Airbnb, dining at Local Cafe, and getting around by Bike.",
                travelSummary);
    }

    @Test
    public void testDestinationAndNotesCombination() {
        // Arrange
        TravelPost post = new TravelPost("2024-06-10", "2024-06-20", "Paris", "Hotel", "French Bistro", "Metro", "Visiting the Eiffel Tower and museums.");

        // Act
        String destinationAndNotes = post.getDestination() + ": " + post.getNotes();

        // Assert
        assertEquals("Destination and notes should concatenate correctly",
                "Paris: Visiting the Eiffel Tower and museums.",
                destinationAndNotes);
    }



}
