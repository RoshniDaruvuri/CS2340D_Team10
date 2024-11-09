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
 * Singleton class for managing accommodations logs in a Firebase database.
 */
public class AccommodationsDatabase {
    private static AccommodationsDatabase instance;
    private final DatabaseReference databaseReference;
    private final List<AccommodationsLog> accommodationsLogs;
    private ValueEventListener eventListener;

    /**
     * Private constructor for the singleton pattern.
     */
    private AccommodationsDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("accommodationsLogs");
        accommodationsLogs = new ArrayList<>();
        initializeDatabase();
    }

    /**
     * Retrieves the singleton instance of AccommodationsDatabase.
     *
     * @param context the context used to initialize the instance.
     * @return the singleton instance of AccommodationsDatabase.
     */
    public static synchronized AccommodationsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AccommodationsDatabase();
        }
        return instance;
    }

    /**
     * Initializes database operations, loading existing logs.
     */
    private void initializeDatabase() {
        loadAccommodationsLogsFromFirebase();
    }

    /**
     * Adds a new accommodations log to the database.
     *
     * @param log the AccommodationsLog object to be added.
     */
    public void addAccommodationsLog(AccommodationsLog log) {
        String key = databaseReference.push().getKey();
        if (key != null) {
            databaseReference.child(key).setValue(log);
        }
    }

    /**
     * Loads accommodations logs from Firebase and listens for updates.
     */
    private void loadAccommodationsLogsFromFirebase() {
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accommodationsLogs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AccommodationsLog log = dataSnapshot.getValue(AccommodationsLog.class);
                    if (log != null) {
                        accommodationsLogs.add(log);
                    }
                }
                Log.d("AccommodationsDatabase", "Logs loaded successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AccommodationsDatabase", "Failed to load logs", error.toException());
            }
        };
        databaseReference.addValueEventListener(eventListener);
    }

    /**
     * Calculates the number of days between check-in and check-out dates.
     *
     * @param checkin  the check-in date in the format "yyyy-MM-dd".
     * @param checkout the check-out date in the format "yyyy-MM-dd".
     * @return the number of days, or 0 if the date format is invalid.
     */
    public int calculateDuration(String checkin, String checkout) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate start = LocalDate.parse(checkin, formatter);
            LocalDate end = LocalDate.parse(checkout, formatter);
            return (int) Math.max(ChronoUnit.DAYS.between(start, end), 0);
        } catch (DateTimeParseException e) {
            Log.e("AccommodationsDatabase", "Invalid date format", e);
            return 0;
        }
    }

    /**
     * Gets the list of all loaded accommodations logs.
     *
     * @return the list of accommodations logs.
     */
    public List<AccommodationsLog> getAccommodationsLogs() {
        return new ArrayList<>(accommodationsLogs);
    }
}
