package com.example.wandersyncteam10.view;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for managing travel logs in a Firebase database.
 */
public class DestinationDatabase {
    private static DestinationDatabase instance;
    private final DatabaseReference databaseReference;
    private final List<TravelLog> travelLogs;
    private ValueEventListener eventListener;

    /**
     * Private constructor for the singleton pattern.
     */
    private DestinationDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("travelLogs");
        travelLogs = new ArrayList<>();
        initializeDatabase();
    }

    /**
     * Retrieves the singleton instance of DestinationDatabase.
     *
     * @param context the context used to initialize the instance.
     * @return the singleton instance of DestinationDatabase.
     */
    public static synchronized DestinationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new DestinationDatabase();
        }
        return instance;
    }

    /**
     * Initializes database operations, loading existing logs and adding prepopulated logs.
     */
    private void initializeDatabase() {
        loadTravelLogsFromFirebase();
        addPrepopulatedTravelLogs();
    }

    /**
     * Calculates the duration in days between two dates.
     *
     * @param startDate the start date in the format "yyyy-MM-dd".
     * @param endDate   the end date in the format "yyyy-MM-dd".
     * @return the duration in days, or 0 if the date format is invalid.
     */
    private int calculateDuration(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            return (int) Math.max(ChronoUnit.DAYS.between(start, end), 0);
        } catch (DateTimeParseException e) {
            Log.e("DestinationDatabase", "Invalid date format", e);
            return 0;
        }
    }

    /**
     * Adds a new travel log to the database.
     *
     * @param location    the location of the travel log.
     * @param startDate   the start date of the travel log in the format "yyyy-MM-dd".
     * @param endDate     the end date of the travel log in the format "yyyy-MM-dd".
     * @param invitedUser the name of the invited user for the travel log.
     */
    public void addTravelLog(String location, String startDate, String endDate, String invitedUser) {
        int duration = calculateDuration(startDate, endDate);
        TravelLog newLog = new TravelLog(location, startDate, endDate, duration, invitedUser);
        databaseReference.push().setValue(newLog);
        Log.d("DestinationDatabase", "Added travel log: " + newLog);
    }

    /**
     * Adds a calculated duration travel log to the database.
     *
     * @param startDate the start date of the travel log in the format "yyyy-MM-dd".
     * @param endDate   the end date of the travel log in the format "yyyy-MM-dd".
     */
    public void addCalculatedDuration(String startDate, String endDate) {
        int duration = calculateDuration(startDate, endDate);
        TravelLog calculatedLog = new TravelLog("Calculated Duration", startDate, endDate, duration);
        databaseReference.push().setValue(calculatedLog);
        Log.d("DestinationDatabase", "Added calculated duration: " + calculatedLog);
    }

    /**
     * Gets the total vacation days from all travel logs.
     *
     * @return the total vacation days.
     */
    public int getTotalVacationDays() {
        int totalDays = travelLogs.stream().mapToInt(TravelLog::getDuration).sum();
        Log.d("DestinationDatabase", "Total vacation days: " + totalDays);
        return totalDays;
    }

    /**
     * Loads travel logs from Firebase in real-time.
     */
    private void loadTravelLogsFromFirebase() {
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                travelLogs.clear();
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    TravelLog travelLog = logSnapshot.getValue(TravelLog.class);
                    if (travelLog != null) {
                        travelLogs.add(travelLog);
                        Log.d("DestinationDatabase", "Added travel log: " + travelLog.getLocation());
                    }
                }
                notifyDataChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DestinationDatabase", "Error loading data from Firebase", error.toException());
            }
        });
    }

    /**
     * Adds prepopulated travel logs if the database is empty.
     */
    private void addPrepopulatedTravelLogs() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    addSampleTravelLogs();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DestinationDatabase", "Error checking existing logs", error.toException());
            }
        });
    }

    /**
     * Helper method to add sample travel logs.
     */
    private void addSampleTravelLogs() {
        databaseReference.push().setValue(new TravelLog("Paris", "2024-01-01", "2024-01-07", 7, "Amanda"));
        databaseReference.push().setValue(new TravelLog("Tokyo", "2024-02-15", "2024-02-22", 7, "Ashley"));
    }

    /**
     * Retrieves the list of travel logs.
     *
     * @return a list of travel logs.
     */
    public List<TravelLog> getTravelLogs() {
        return new ArrayList<>(travelLogs);
    }

    /**
     * Notifies the UI of data changes.
     * Implement UI update logic, e.g., notify RecyclerView adapter.
     */
    private void notifyDataChanged() {
        // Implement UI update logic here
    }

    /**
     * Removes the Firebase listener to prevent memory leaks.
     */
    public void removeListener() {
        if (eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}
