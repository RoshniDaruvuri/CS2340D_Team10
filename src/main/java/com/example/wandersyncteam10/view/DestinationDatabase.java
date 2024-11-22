package com.example.wandersyncteam10.view;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import android.content.Context;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class DestinationDatabase {
    private static DestinationDatabase instance;
    private final List<TravelLog> travelLogs;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private DestinationDatabase() {
        travelLogs = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance(); // Get Firebase auth instance
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        // Prepopulate with 2 entries
        travelLogs.add(new TravelLog("New York", "2024-01-10", "2024-01-20"));
        travelLogs.add(new TravelLog("Tokyo", "2024-05-01", "2024-05-15"));
    }

    public static synchronized DestinationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new DestinationDatabase();
        }
        return instance;
    }

    // Save travel log to Firestore
    public void addTravelLog(String location, String startDate, String endDate) {
        travelLogs.add(new TravelLog(location, startDate, endDate));

        // Get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Create a travel log map to send to Firestore
            Map<String, Object> travelLog = new HashMap<>();
            travelLog.put("location", location);
            travelLog.put("startDate", startDate);
            travelLog.put("endDate", endDate);

            // Reference to user's collection in Firestore
            CollectionReference userVacationsRef = db.collection("users").document(userId).collection("vacations");

            // Add the travel log to Firestore
            userVacationsRef.add(travelLog)
                    .addOnSuccessListener(documentReference -> {
                        // Log successfully added
                        System.out.println("Vacation successfully added!");
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                        System.out.println("Error adding vacation: " + e.getMessage());
                    });
        }
    }

    public List<TravelLog> getTravelLogs() {
        return travelLogs;
    }
}
