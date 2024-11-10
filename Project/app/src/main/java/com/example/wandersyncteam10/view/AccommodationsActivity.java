package com.example.wandersyncteam10.view;

import android.content.Intent;
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
import java.util.List;

public class AccommodationsActivity extends AppCompatActivity {

    private EditText checkInInput, checkOutInput, locationInput, numRoomsInput, roomTypeInput;
    private Button logAccommodationButton, logFormButton;
    private ImageButton toggleFormButton;

    private FirebaseUser currentUser;
    private DatabaseReference accommodationsLogsRef;
    private ListView accommodationsLogsList;
    private View formLayout;
    private SortingStrategy sortingStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations);

        // Initialize UI elements
        checkInInput = findViewById(R.id.check_in);
        checkOutInput = findViewById(R.id.check_out);
        locationInput = findViewById(R.id.location);
        numRoomsInput = findViewById(R.id.num_rooms);
        roomTypeInput = findViewById(R.id.room_type);
        accommodationsLogsList = findViewById(R.id.accommodations_logs_list);
        formLayout = findViewById(R.id.form_layout);

        logAccommodationButton = findViewById(R.id.log_accommodation);
        logFormButton = findViewById(R.id.log_accom); // Button to store data in Firebase
        toggleFormButton = findViewById(R.id.toggle_form_button);

        formLayout.setVisibility(View.GONE);
        logFormButton.setVisibility(View.GONE);
        // Apply edge-to-edge UI padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set listeners for the buttons
        toggleFormButton.setOnClickListener(v -> {
            if (formLayout.getVisibility() == View.VISIBLE) {
                formLayout.setVisibility(View.GONE);
                logFormButton.setVisibility(View.GONE);
            } else {
                formLayout.setVisibility(View.VISIBLE);
                logFormButton.setVisibility(View.VISIBLE);
            }
        });

        logFormButton.setOnClickListener(v -> logAccommodation());

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

        Button sortByCheckInButton = findViewById(R.id.sort_checkin_button);
        sortByCheckInButton.setOnClickListener(v -> {
            sortingStrategy = new CheckInSort(); // Set to check-in sorting strategy
            fetchAndDisplayData(); // Fetch and display sorted data
        });

        // Dashboard button listeners (keeping these unchanged)
        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent(AccommodationsActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button2).setOnClickListener(view -> {
            Intent intent = new Intent(AccommodationsActivity.this, DestinationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent intent = new Intent(AccommodationsActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button4).setOnClickListener(view -> {
            Intent intent = new Intent(AccommodationsActivity.this, AccommodationsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button5).setOnClickListener(view -> {
            Intent intent = new Intent(AccommodationsActivity.this, TransportationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button6).setOnClickListener(view -> {
            Intent intent = new Intent(AccommodationsActivity.this, TravelActivity.class);
            startActivity(intent);
        });
    }

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

                // Apply the sorting strategy if selected
                if (sortingStrategy != null) {
                    sortingStrategy.sort(logs);
                }

                // Convert logs to strings for display
                List<String> formattedLogs = new ArrayList<>();
                for (AccommodationsLog log : logs) {
                    String logDetails = "Check-in: " + log.getCheckin() + "\n" +
                            "Check-out: " + log.getCheckout() + "\n" +
                            "Location: " + log.getLocation() + "\n" +
                            "Rooms: " + log.getRoomnum() + "\n" +
                            "Room Type: " + log.getRoomtype();
                    formattedLogs.add(logDetails);
                }

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

    private void fetchAndDisplayData() {
        accommodationsLogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AccommodationsLog> logs = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    AccommodationsLog log = data.getValue(AccommodationsLog.class);
                    if (log != null) {
                        logs.add(log);
                    }
                }

                // Apply the sorting strategy if selected
                if (sortingStrategy != null) {
                    sortingStrategy.sort(logs);
                }

                // Convert logs to strings for display
                List<String> formattedLogs = new ArrayList<>();
                for (AccommodationsLog log : logs) {
                    String logDetails = "Check-in: " + log.getCheckin() + "\n" +
                            "Check-out: " + log.getCheckout() + "\n" +
                            "Location: " + log.getLocation() + "\n" +
                            "Rooms: " + log.getRoomnum() + "\n" +
                            "Room Type: " + log.getRoomtype();
                    formattedLogs.add(logDetails);
                }

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
                        logFormButton.setVisibility(View.GONE); // Hide the log button
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
        return true;
    }
}


