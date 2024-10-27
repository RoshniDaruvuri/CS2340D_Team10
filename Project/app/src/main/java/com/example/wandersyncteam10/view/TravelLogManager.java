package com.example.wandersyncteam10.view;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages travel log entries and interactions with Firebase.
 */
public class TravelLogManager {

    private DatabaseReference travelLogReference;
    private DatabaseReference possibleDurationReference;
    private FirebaseAuth mAuth;

    /**
     * Constructor to initialize Firebase authentication and database references.
     */
    public TravelLogManager() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database References
        travelLogReference = FirebaseDatabase.getInstance().getReference("travelLogs");
        possibleDurationReference = FirebaseDatabase.getInstance().getReference("calculatedDurations");
    }

    /**
     * Saves a travel log entry to Firebase.
     *
     * @param location The location of the travel log entry.
     * @param startDate The start date of the travel log entry.
     * @param endDate The end date of the travel log entry.
     * @param duration The duration of the travel in days.
     */
    public void saveTravelLog(String location, String startDate, String endDate, int duration) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        // Log user ID for debugging
        Log.d("USER_ID", "Current User ID: " + userId);

        if (userId == null) {
            Log.e("AUTH", "User is not authenticated!");
            return; // Exit if the user is not authenticated
        }

        String logId = travelLogReference.child(userId).push().getKey();
        TravelLog travelLog = new TravelLog(location, startDate, endDate, duration);

        // Save the travel log data under the user's ID
        if (logId != null) {
            travelLogReference.child(userId).child(logId).setValue(travelLog)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("TRAVEL_LOG", "Travel log saved successfully under User ID: " + userId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("TRAVEL_LOG", "Failed to save travel log: " + e.getMessage());
                    });
        }
    }

    /**
     * Saves a possible duration entry to Firebase.
     *
     * @param possibleDuration The possible duration to save.
     */
    public void savePossibleDuration(String possibleDuration) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        // Log user ID for debugging
        Log.d("USER_ID", "Current User ID: " + userId);

        if (userId == null) {
            Log.e("AUTH", "User is not authenticated!");
            return; // Exit if the user is not authenticated
        }

        String durationId = possibleDurationReference.child(userId).push().getKey();

        if (durationId != null) {
            Map<String, Object> durationData = new HashMap<>();
            durationData.put("possibleDuration", possibleDuration);
            durationData.put("userId", userId); // Include userId

            possibleDurationReference.child(userId).child(durationId).setValue(durationData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("POSSIBLE_DURATION", "Possible duration saved successfully under User ID: " + userId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("POSSIBLE_DURATION", "Failed to save possible duration: " + e.getMessage());
                    });
        }
    }
}
