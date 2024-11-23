package com.example.wandersyncteam10.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wandersyncteam10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private LinearLayout formLayout;
    private EditText locationInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private EditText startInput;
    private EditText endInput;
    private EditText addedUser;
    private ListView travelLogsList;
    private TextView totalVacationDaysView;
    private TextView durationOutcome;
    private Button calculateDurationButton;
    private Button calculateButton;
    private FirebaseUser currentUser;
    private DatabaseReference travelLogsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        // Initialize UI elements
        initializeUI();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            travelLogsRef = FirebaseDatabase.getInstance()
                    .getReference("travelLogs")
                    .child(currentUser.getUid());
            updateTravelLogsList();
            updateTotalVacationDays();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        configureButtons();
    }

    /**
     * Initializes UI elements and sets default visibility.
     */
    private void initializeUI() {
        formLayout = findViewById(R.id.form_layout);
        locationInput = findViewById(R.id.location_input);
        startDateInput = findViewById(R.id.start_date_input);
        endDateInput = findViewById(R.id.end_date_input);
        travelLogsList = findViewById(R.id.travel_logs_list);
        totalVacationDaysView = findViewById(R.id.total_vacation_days);
        startInput = findViewById(R.id.start_input);
        endInput = findViewById(R.id.end_input);
        durationOutcome = findViewById(R.id.duration_outcome);
        calculateDurationButton = findViewById(R.id.calculate_duration_button);
        calculateButton = findViewById(R.id.calculate_button);
        addedUser = findViewById(R.id.text_invite);

        formLayout.setVisibility(View.GONE);
    }

    /**
     * Configures button click listeners for form interactions.
     */
    private void configureButtons() {
        Button logTravelButton = findViewById(R.id.log_travel_button);
        Button calculateVacationButton = findViewById(R.id.calculate_vacation_button);

        logTravelButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            clearInputs();
        });

        calculateVacationButton.setOnClickListener(v -> handleFormSubmission());
        calculateDurationButton.setOnClickListener(v -> showDurationInputFields());
        calculateButton.setOnClickListener(v -> calculateDuration());
    }

    /**
     * Handles form submission for logging travel.
     */
    private void handleFormSubmission() {
        String location = locationInput.getText().toString();
        String startDate = startDateInput.getText().toString();
        String endDate = endDateInput.getText().toString();
        String invitedUser = addedUser.getText().toString();

        if (location.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
            Toast.makeText(this, "Invalid date format. Use YYYY-MM-DD.", Toast.LENGTH_SHORT).show();
            return;
        }

        saveTravelLog(location, startDate, endDate, invitedUser);
        updateTravelLogsList();
        updateTotalVacationDays();
        formLayout.setVisibility(View.GONE);
    }

    /**
     * Clears input fields.
     */
    private void clearInputs() {
        locationInput.setText("");
        startDateInput.setText("");
        endDateInput.setText("");
        addedUser.setText("");
    }

    /**
     * Displays duration input fields.
     */
    private void showDurationInputFields() {
        startInput.setVisibility(View.VISIBLE);
        endInput.setVisibility(View.VISIBLE);
        durationOutcome.setVisibility(View.VISIBLE);
        calculateButton.setVisibility(View.VISIBLE);
    }

    /**
     * Calculates and displays the travel duration.
     */
    private void calculateDuration() {
        String startDate = startInput.getText().toString();
        String endDate = endInput.getText().toString();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Both start and end dates are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration = calculateTravelDuration(startDate, endDate);
        durationOutcome.setText(duration + " days");
    }

    /**
     * Calculates the duration in days between two dates.
     *
     * @param startDate The start date in YYYY-MM-DD format.
     * @param endDate   The end date in YYYY-MM-DD format.
     * @return The duration in days.
     */
    private int calculateTravelDuration(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return (int) ChronoUnit.DAYS.between(start, end);
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid date format. Use YYYY-MM-DD.", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    /**
     * Validates if a date string is in the correct format.
     *
     * @param date The date string to validate.
     * @return True if the date is valid, false otherwise.
     */
    private boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Saves a travel log to Firebase.
     *
     * @param location    The travel location.
     * @param startDate   The start date.
     * @param endDate     The end date.
     * @param invitedUser The invited user's name.
     */
    private void saveTravelLog(String location, String startDate, String endDate, String invitedUser) {
        if (currentUser == null) {
            return;
        }

        String key = travelLogsRef.push().getKey();
        if (key != null) {
            HashMap<String, String> travelData = new HashMap<>();
            travelData.put("location", location);
            travelData.put("startDate", startDate);
            travelData.put("endDate", endDate);
            travelData.put("invitedUser", invitedUser);

            travelLogsRef.child(key).setValue(travelData)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Travel log saved.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to save log.", Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Updates the travel logs list view.
     */
    private void updateTravelLogsList() {
        if (currentUser == null) {
            return;
        }

        travelLogsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> travelLogs = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String location = data.child("location").getValue(String.class);
                    String startDate = data.child("startDate").getValue(String.class);
                    String endDate = data.child("endDate").getValue(String.class);
                    String invitedUser = data.child("invitedUser").getValue(String.class);
                    int duration = calculateTravelDuration(startDate, endDate);

                    travelLogs.add(location + " (" + duration + " days) - Invited: " + invitedUser);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(DestinationActivity.this,
                        android.R.layout.simple_list_item_1, travelLogs);
                travelLogsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DestinationActivity.this, "Failed to load logs.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Updates total vacation days display.
     */
    private void updateTotalVacationDays() {
        // Placeholder method to update vacation days
    }
}
