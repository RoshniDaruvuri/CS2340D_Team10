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

public class DestinationDatabase {
    private static DestinationDatabase instance;
    private final DatabaseReference databaseReference;
    private final List<TravelLog> travelLogs = new ArrayList<>();
    private ValueEventListener eventListener;

    // Constructor that initializes Firebase reference and starts loading travel logs
    private DestinationDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("travelLogs");
        // Fetch existing logs from Firebase in real-time
        loadTravelLogsFromFirebase();
        // Add prepopulated travel logs
        addPrepopulatedTravelLogs();
    }

    // Singleton pattern for getting the instance of the database
    public static synchronized DestinationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new DestinationDatabase();
        }
        return instance;
    }

    // Method to calculate the duration between two dates
    private int calculateDuration(String startDate, String endDate) {
        try {
            // Parse the input dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            // Calculate the duration in days
            long daysBetween = ChronoUnit.DAYS.between(start, end);

            // Ensure the duration is non-negative (in case dates are reversed)
            return (int) Math.max(daysBetween, 0);
        } catch (DateTimeParseException e) {
            Log.e("DestinationDatabase", "Invalid date format", e);
            return 0;
        }
    }

    // Method to add a new travel log to Firebase
    public void addTravelLog(String location, String startDate, String endDate) {
        int duration = calculateDuration(startDate, endDate);
        Log.d("DestinationDatabase", "Calculated duration: " + duration);
        TravelLog newLog = new TravelLog(location, startDate, endDate, duration);
        // Push new entry to Firebase
        databaseReference.push().setValue(newLog);
    }

    // New method to add calculated duration directly to the database
    public void addCalculatedDuration(String startDate, String endDate) {
        int duration = calculateDuration(startDate, endDate);
        Log.d("DestinationDatabase", "Calculated duration: " + duration);

        // Create a new TravelLog object with calculated duration
        TravelLog calculatedLog = new TravelLog("Calculated Duration", startDate, endDate, duration);

        // Push the calculated duration to Firebase
        databaseReference.push().setValue(calculatedLog);
    }

    public int getTotalVacationDays() {
        int totalDays = 0;
        // Sum up the duration of all travel logs
        for (TravelLog log : travelLogs) {
            totalDays += log.getDuration();  // Get the duration from each travel log
        }
        Log.d("DestinationDatabase", "Total vacation days: " + totalDays);
        return totalDays;
    }

    // Method to load travel logs from Firebase in real-time
    private void loadTravelLogsFromFirebase() {
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                travelLogs.clear();  // Clear the local list before updating
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    TravelLog travelLog = logSnapshot.getValue(TravelLog.class);
                    if (travelLog != null) {
                        travelLogs.add(travelLog);
                        // Log to check if logs are successfully added
                        Log.d("DestinationDatabase", "Added travel log: " + travelLog.getLocation());
                    }
                }
                // Notify the UI that data has changed (if using RecyclerView or similar)
                notifyDataChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log an error message if Firebase data retrieval fails
                Log.e("DestinationDatabase", "Error loading data from Firebase", error.toException());
            }
        });
    }

    // Method to add prepopulated travel logs
    private void addPrepopulatedTravelLogs() {
        // Check if the database is empty before adding the logs
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Only add if there are no existing logs
                    TravelLog log1 = new TravelLog("Paris", "2024-01-01", "2024-01-07", 7);
                    TravelLog log2 = new TravelLog("Tokyo", "2024-02-15", "2024-02-22", 7);

                    databaseReference.push().setValue(log1);
                    databaseReference.push().setValue(log2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DestinationDatabase", "Error checking existing logs", error.toException());
            }
        });
    }

    // Retrieve travel logs from the locally updated list (synced with Firebase)
    public List<TravelLog> getTravelLogs() {
        return travelLogs;
    }

    // Method to notify the UI that data has changed (you can call this from your activity or fragment)
    private void notifyDataChanged() {
        // This is where you'd update your RecyclerView or UI component
        // For example, if using RecyclerView, you'd call:
        // adapter.notifyDataSetChanged();
    }

    // Properly remove the listener when the activity is destroyed to avoid memory leaks
    public void removeListener() {
        if (eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}

