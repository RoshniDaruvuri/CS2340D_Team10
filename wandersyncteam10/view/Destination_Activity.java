package com.example.wandersyncteam10.view;  // Fix: Ensuring correct package path

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersyncteam10.R;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {  // Fix: Renamed class to follow naming conventions

    private LinearLayout formLayout;
    private EditText locationInput;
    private EditText startDateInput;  // Fix: Declarations on separate lines
    private EditText endDateInput;
    private EditText startInput;
    private EditText endInput;
    private EditText durationOutcome;
    private ListView travelLogsList;
    private Button calculateDurationButton;
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // log travel !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        formLayout = findViewById(R.id.form_layout);
        locationInput = findViewById(R.id.location_input);
        startDateInput = findViewById(R.id.start_date_input);
        endDateInput = findViewById(R.id.end_date_input);
        travelLogsList = findViewById(R.id.travel_logs_list);
        calculateDurationButton = findViewById(R.id.calculate_duration_button);

        startInput = findViewById(R.id.start_input);
        endInput = findViewById(R.id.end_input);
        durationOutcome = findViewById(R.id.duration_outcome);

        startInput.setVisibility(View.GONE);
        endInput.setVisibility(View.GONE);
        durationOutcome.setVisibility(View.GONE);

        calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setVisibility(View.GONE);  // Initially hidden

        Button logTravelButton = findViewById(R.id.log_travel_button);
        Button calculateVacationButton = findViewById(R.id.calculate_vacation_button);

        formLayout.setVisibility(View.GONE);

        updateTravelLogsList();

        // Show the form when "Log Travel" is clicked
        logTravelButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            calculateDurationButton.setVisibility(View.GONE);

            // Hide the duration input fields when logging travel
            startInput.setVisibility(View.GONE);
            endInput.setVisibility(View.GONE);
            durationOutcome.setVisibility(View.GONE);
            calculateButton.setVisibility(View.GONE);
        });

        calculateVacationButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            String startDate = startDateInput.getText().toString();
            String endDate = endDateInput.getText().toString();

            // validation for form input
            if (location.isEmpty()) {
                Toast.makeText(DestinationActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save travel data in the Singleton Database
            DestinationDatabase.getInstance(DestinationActivity.this).addTravelLog(location, startDate, endDate);
            Toast.makeText(DestinationActivity.this, "Vacation logged successfully!", Toast.LENGTH_SHORT).show();

            // Update the travel logs list
            updateTravelLogsList();

            formLayout.setVisibility(View.GONE);
            calculateDurationButton.setVisibility(View.VISIBLE);
        });

        // Handle the Calculate Duration button click to show the duration input fields
        calculateDurationButton.setOnClickListener(v -> {
            startInput.setVisibility(View.VISIBLE);
            endInput.setVisibility(View.VISIBLE);
            durationOutcome.setVisibility(View.VISIBLE);
            calculateButton.setVisibility(View.VISIBLE);
        });

        calculateButton.setOnClickListener(v -> {
            String startDateInputText = startInput.getText().toString();
            String endDateInputText = endInput.getText().toString();

            // Validate inputs
            if (startDateInputText.isEmpty() || endDateInputText.isEmpty()) {
                Toast.makeText(DestinationActivity.this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = calculateTravelDuration(startDateInputText, endDateInputText);
            durationOutcome.setText(duration + " days");
        });

        // DASHBOARD BUTTONS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, Logistics_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button2).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, DestinationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, Dining_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button4).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, Accommodations_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button5).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, Transportation_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button6).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, Travel_Activity.class);
            startActivity(intent);
        });
    }

    // Update the travel logs list
    private void updateTravelLogsList() {
        // Fetch the travel logs from the Singleton Database
        DestinationDatabase db = DestinationDatabase.getInstance(this);
        List<TravelLog> travelLogs = db.getTravelLogs();  // Get the travel logs

        if (travelLogs.isEmpty()) {
            Log.d("DEBUG", "No travel logs found in database");
        } else {
            Log.d("DEBUG", "Found " + travelLogs.size() + " travel logs.");
        }

        List<String> travelLogStrings = new ArrayList<>();
        for (TravelLog log : travelLogs) {
            String duration = getTravelDuration(log.getStartDate(), log.getEndDate()) + " days";
            travelLogStrings.add(log.getLocation() + " (" + duration + ")");
        }

        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, travelLogStrings);
        travelLogsList.setAdapter(adapter);
    }

    // Calculate travel duration between two dates
    private int getTravelDuration(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return (int) ChronoUnit.DAYS.between(start, end);
        } catch (DateTimeParseException e) {
            return 0;  // If dates are invalid, return 0
        }
    }

    // Calculate duration based on start and end dates input
    private int calculateTravelDuration(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return (int) ChronoUnit.DAYS.between(start, end);
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid date format. Please use YYYY-MM-DD.", Toast.LENGTH_SHORT).show();
            return 0;  // Return 0 for invalid dates
        }
    }
}
