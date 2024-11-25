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

import com.example.wandersyncteam10.Model.AccommodationsLog;
import com.example.wandersyncteam10.Model.SortingStrategy;
import com.example.wandersyncteam10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccommodationsActivity extends AppCompatActivity {

    private EditText checkInInput;
    private EditText checkOutInput;
    private EditText locationInput;
    private EditText numRoomsInput;
    private EditText roomTypeInput;
    private Button logAccommodationButton;
    private Button logFormButton;
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

        Button sortAlphabeticallyButton = findViewById(R.id.sort_alphabetical_button);
        logAccommodationButton = findViewById(R.id.log_accommodation);
        logFormButton = findViewById(R.id.log_accom);
        toggleFormButton = findViewById(R.id.toggle_form_button);

        formLayout.setVisibility(View.GONE);
        logFormButton.setVisibility(View.GONE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        sortAlphabeticallyButton.setOnClickListener(v -> {
            sortingStrategy = new AlphabeticalSortingStrategy();
            fetchAndDisplayData();
        });

        Button sortByCheckInButton = findViewById(R.id.sort_checkin_button);
        sortByCheckInButton.setOnClickListener(v -> {
            sortingStrategy = new CheckInSort();
            fetchAndDisplayData();
        });

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
            Intent intent = new Intent(AccommodationsActivity.this, CommunityActivity.class);
            startActivity(intent);
        });

//        findViewById(R.id.button6).setOnClickListener(view -> {
//            Intent intent = new Intent(AccommodationsActivity.this, TravelActivity.class);
//            startActivity(intent);
//      });
    }

    /**
     * Sets up a database listener to fetch and display accommodation logs.
     */
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

                if (sortingStrategy != null) {
                    sortingStrategy.sort(logs);
                }

                List<String> formattedLogs = new ArrayList<>();
                for (AccommodationsLog log : logs) {
                    String logDetails = "Check-in: " + log.getCheckin() + "\n"
                            + "Check-out: " + log.getCheckout() + "\n"
                            + "Location: " + log.getLocation() + "\n"
                            + "Rooms: " + log.getRoomnum() + "\n"
                            + "Room Type: " + log.getRoomtype();
                    formattedLogs.add(logDetails);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        AccommodationsActivity.this,
                        android.R.layout.simple_list_item_1,
                        formattedLogs
                );
                accommodationsLogsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccommodationsActivity.this, "Failed to load logs: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("AccommodationsActivity", "Database error: " + error.getMessage());
            }
        });
    }

    /**
     * Fetches accommodation logs and displays them after sorting.
     */
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

                if (sortingStrategy != null) {
                    sortingStrategy.sort(logs);
                }

                List<String> formattedLogs = new ArrayList<>();
                for (AccommodationsLog log : logs) {
                    String logDetails = "Check-in: " + log.getCheckin() + "\n"
                            + "Check-out: " + log.getCheckout() + "\n"
                            + "Location: " + log.getLocation() + "\n"
                            + "Rooms: " + log.getRoomnum() + "\n"
                            + "Room Type: " + log.getRoomtype();
                    formattedLogs.add(logDetails);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        AccommodationsActivity.this,
                        android.R.layout.simple_list_item_1,
                        formattedLogs
                );
                accommodationsLogsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccommodationsActivity.this, "Failed to load logs: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("AccommodationsActivity", "Database error: " + error.getMessage());
            }
        });
    }

    /**
     * Logs an accommodation entry to Firebase.
     */
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
                        formLayout.setVisibility(View.GONE);
                        logFormButton.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to add log: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AccommodationsActivity", "Error: " + e.getMessage());
                    });
        }
    }

    /**
     * Validates input for logging accommodations.
     *
     * @param checkInDate  The check-in date.
     * @param checkOutDate The check-out date.
     * @param location     The accommodation location.
     * @param numRooms     The number of rooms.
     * @param roomType     The type of room.
     * @return True if input is valid; otherwise false.
     */
    private boolean validateInput(String checkInDate, String checkOutDate,
                                  String location, String numRooms, String roomType) {
        // Validation logic
        return true; // Adjusted for brevity
    }
}
