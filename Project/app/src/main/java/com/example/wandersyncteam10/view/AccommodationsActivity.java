package com.example.wandersyncteam10.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersyncteam10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AccommodationsActivity extends AppCompatActivity {

    private EditText checkInInput, checkOutInput, locationInput, numRoomsInput, roomTypeInput;
    private Button logAccommodationButton, logNewHotelButton;
    private ImageButton toggleFormButton;

    private FirebaseUser currentUser;
    private DatabaseReference accommodationsLogsRef;
    private ListView accommodationsLogsList;
    private View formLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations);
        toggleFormButton = findViewById(R.id.toggle_form_button);

        // Apply edge-to-edge UI padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize input fields
        checkInInput = findViewById(R.id.check_in);
        checkOutInput = findViewById(R.id.check_out);
        locationInput = findViewById(R.id.location);
        numRoomsInput = findViewById(R.id.num_rooms);
        roomTypeInput = findViewById(R.id.room_type);
        accommodationsLogsList = findViewById(R.id.accommodations_logs_list);
        formLayout = findViewById(R.id.form_layout); // Reference to the form layout

        // Initialize buttons
        logAccommodationButton = findViewById(R.id.log_accommodation);
        toggleFormButton = findViewById(R.id.toggle_form_button);

        // Initially hide the form
        formLayout.setVisibility(View.GONE);

        // Set listeners for form toggle and log button
        toggleFormButton.setOnClickListener(v -> {
            formLayout.setVisibility(formLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        logAccommodationButton.setOnClickListener(v -> logAccommodation());

        // Initialize Firebase authentication and get the current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            accommodationsLogsRef = FirebaseDatabase.getInstance()
                    .getReference("accommodationsLogs")
                    .child(currentUser.getUid());
            setupDatabaseListener();
        } else {
            Toast.makeText(this, "User not logged in. Please log in to continue.", Toast.LENGTH_SHORT).show();
            Log.e("AccommodationsActivity", "User is null");
        }
    }

//    private void setupDatabaseListener() {
//        accommodationsLogsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<String> logs = new ArrayList<>();
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    AccommodationsLog log = data.getValue(AccommodationsLog.class);
//                    if (log != null) {
//                        LocalDate checkoutDate = LocalDate.parse(log.getCheckout(), DateTimeFormatter.ISO_LOCAL_DATE);
//                        LocalDate today = LocalDate.now();
//
//
//                        LocalDate.parse(log.getCheckout(), DateTimeFormatter.ISO_LOCAL_DATE);
//
//                        String logDetails = "Check-in: " + log.getCheckin() + "\n" +
//                                "Check-out: " + log.getCheckout() + "\n" +
//                                "Location: " + log.getLocation() + "\n" +
//                                "Rooms: " + log.getRoomnum() + "\n" +
//                                "Room Type: " + log.getRoomtype();
//
//                        if (checkoutDate.isBefore(today)) {
//                            logDetails += "\nStatus: expired";
//                            //logDetails += "\"\\u001B[1mStatus: expired\\u001B[0m\"";
//                        }
//                        logs.add(logDetails);
//                    }
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(AccommodationsActivity.this, android.R.layout.simple_list_item_1, logs);
//                accommodationsLogsList.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(AccommodationsActivity.this, "Failed to load logs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("AccommodationsActivity", "Database error: " + error.getMessage());
//            }
//        });
//    }

    private void setupDatabaseListener() {
        accommodationsLogsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AccommodationsLog> logs = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    AccommodationsLog log = data.getValue(AccommodationsLog.class);
                    if (log != null) {
                        logs.add(log);
                    }
                }

                // Sort the logs by check-in date and then by check-out date
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                Collections.sort(logs, new Comparator<AccommodationsLog>() {
                    @Override
                    public int compare(AccommodationsLog log1, AccommodationsLog log2) {
                        LocalDate checkinDate1 = LocalDate.parse(log1.getCheckin(), formatter);
                        LocalDate checkinDate2 = LocalDate.parse(log2.getCheckin(), formatter);
                        int checkinComparison = checkinDate1.compareTo(checkinDate2);

                        // If check-in dates are the same, compare by check-out date
                        if (checkinComparison == 0) {
                            LocalDate checkoutDate1 = LocalDate.parse(log1.getCheckout(), formatter);
                            LocalDate checkoutDate2 = LocalDate.parse(log2.getCheckout(), formatter);
                            return checkoutDate1.compareTo(checkoutDate2);
                        }

                        return checkinComparison;
                    }
                });

                // Convert sorted logs to strings for display
                List<String> formattedLogs = new ArrayList<>();
                LocalDate today = LocalDate.now();
                for (AccommodationsLog log : logs) {
                    LocalDate checkoutDate = LocalDate.parse(log.getCheckout(), formatter);

                    String logDetails = "Check-in: " + log.getCheckin() + "\n" +
                            "Check-out: " + log.getCheckout() + "\n" +
                            "Location: " + log.getLocation() + "\n" +
                            "Rooms: " + log.getRoomnum() + "\n" +
                            "Room Type: " + log.getRoomtype();

                    if (checkoutDate.isBefore(today) || checkoutDate.isEqual(today)) {
                        logDetails += "\nStatus: expired";
                    }
                    formattedLogs.add(logDetails);
                }

                // Set the formatted logs to the adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AccommodationsActivity.this, android.R.layout.simple_list_item_1, formattedLogs);
                accommodationsLogsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccommodationsActivity.this, "Failed to load logs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AccommodationsActivity", "Database error: " + error.getMessage());
            }
        });
    }


    private void logAccommodation() {
        String checkInDate = checkInInput.getText().toString();
        String checkOutDate = checkOutInput.getText().toString();
        String location = locationInput.getText().toString();
        String numRooms = numRoomsInput.getText().toString();
        String roomType = roomTypeInput.getText().toString();

        if (validateInput(checkInDate, checkOutDate, location, numRooms, roomType)) {
            AccommodationsLog log = new AccommodationsLog(checkInDate, checkOutDate, location, numRooms, roomType);
            accommodationsLogsRef.push().setValue(log)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Log added successfully", Toast.LENGTH_SHORT).show();
                        formLayout.setVisibility(View.GONE); // Hide the form after logging
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to add log: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AccommodationsActivity", "Error: " + e.getMessage());
                    });
        }
    }

    private boolean validateInput(String checkInDate, String checkOutDate, String location, String numRooms, String roomType) {
        // Check for empty fields
        if (checkInDate.isEmpty() || checkOutDate.isEmpty() || location.isEmpty() || numRooms.isEmpty() || roomType.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate date format and order
        try {
            LocalDate checkIn = LocalDate.parse(checkInDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate checkOut = LocalDate.parse(checkOutDate, DateTimeFormatter.ISO_LOCAL_DATE);
            if (checkOut.isBefore(checkIn)) {
                Toast.makeText(this, "Check-out date must be after check-in date", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid date format. Use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate numRooms as a positive integer within a sensible range
        try {
            int rooms = Integer.parseInt(numRooms);
            if (rooms <= 0 || rooms > 50) {
                Toast.makeText(this, "Please enter a reasonable number of rooms (1-50)", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Number of rooms must be numeric", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Limit text length for location and room type to prevent overly long inputs
        if (location.length() > 50) {
            Toast.makeText(this, "Location name is too long (max 50 characters)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (roomType.length() > 30) {
            Toast.makeText(this, "Room type is too long (max 30 characters)", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
