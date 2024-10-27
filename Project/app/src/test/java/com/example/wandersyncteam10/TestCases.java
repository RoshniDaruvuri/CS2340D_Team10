package com.example.wandersyncteam10;

import com.example.wandersyncteam10.view.TravelLog;
import com.example.wandersyncteam10.view.DestinationDatabase;
import com.example.wandersyncteam10.view.Logistics_Activity;
import org.junit.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
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

    @Test
    public void testAddContributorLogic() {
        // Simulate adding contributors without relying on the Activity or Firebase
        List<String> contributorsList = new ArrayList<>();

        // Add a new contributor
        String contributor = "John Doe";
        contributorsList.add(contributor);

        // Verify the contributor was added correctly
        assertEquals("Contributor list size should be 1", 1, contributorsList.size());
        assertTrue("Contributor list should contain 'John Doe'", contributorsList.contains("John Doe"));
    }


    @Test
    public void testSameStartAndEndDateDuration() {
        TravelLog log = new TravelLog("Staycation", "2024-01-01", "2024-01-01", 0);
        int duration = log.getDuration();
        assertEquals("Duration should be 0 days for same start and end date", 0, duration);
    }

}
