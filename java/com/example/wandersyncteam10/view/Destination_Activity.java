package com.example.wandersyncteam10.view;

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

public class Destination_Activity extends AppCompatActivity {

    private LinearLayout formLayout;
    private EditText locationInput, startDateInput, endDateInput, startInput, endInput, durationOutcome;
    private ListView travelLogsList;
    private Button calculateDurationButton, calculateButton, calculateVacationButton;

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
        calculateButton.setVisibility(View.GONE);

        Button logTravelButton = findViewById(R.id.log_travel_button);
        calculateVacationButton = findViewById(R.id.calculate_vacation_button);

        formLayout.setVisibility(View.GONE);

        updateTravelLogsList();

        logTravelButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            calculateDurationButton.setVisibility(View.GONE);
            startInput.setVisibility(View.GONE);
            endInput.setVisibility(View.GONE);
            durationOutcome.setVisibility(View.GONE);
            calculateButton.setVisibility(View.GONE);
        });

        calculateVacationButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            String startDate = startDateInput.getText().toString();
            String endDate = endDateInput.getText().toString();

            if (location.isEmpty()) {
                Toast.makeText(Destination_Activity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                return;
            }

            DestinationDatabase.getInstance(Destination_Activity.this).addTravelLog(location, startDate, endDate);
            Toast.makeText(Destination_Activity.this, "Vacation logged successfully!", Toast.LENGTH_SHORT).show();

            updateTravelLogsList();
            formLayout.setVisibility(View.GONE);
            calculateDurationButton.setVisibility(View.VISIBLE);
        });

        calculateDurationButton.setOnClickListener(v -> {
            startInput.setVisibility(View.VISIBLE);
            endInput.setVisibility(View.VISIBLE);
            durationOutcome.setVisibility(View.VISIBLE);
            calculateButton.setVisibility(View.VISIBLE);
        });

        calculateButton.setOnClickListener(v -> {
            String startDateInputText = startInput.getText().toString();
            String endDateInputText = endInput.getText().toString();

            if (startDateInputText.isEmpty() || endDateInputText.isEmpty()) {
                Toast.makeText(Destination_Activity.this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = calculateTravelDuration(startDateInputText, endDateInputText);
            durationOutcome.setText(duration + " days");
        });

        findViewById(R.id.button).setOnClickListener(view -> startActivity(new Intent(this, Logistics_Activity.class)));
        findViewById(R.id.button3).setOnClickListener(view -> startActivity(new Intent(this, Dining_Activity.class)));
    }

    private void updateTravelLogsList() {
        DestinationDatabase db = DestinationDatabase.getInstance(this);
        List<TravelLog> travelLogs = db.getTravelLogs();

        List<String> travelLogStrings = new ArrayList<>();
        for (TravelLog log : travelLogs) {
            String duration = getTravelDuration(log.getStartDate(), log.getEndDate()) + " days";
            travelLogStrings.add(log.getLocation() + " (" + duration + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, travelLogStrings);
        travelLogsList.setAdapter(adapter);
    }

    private int getTravelDuration(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return (int) ChronoUnit.DAYS.between(start, end);
        } catch (DateTimeParseException e) {
            return 0;
        }
    }

    private int calculateTravelDuration(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return (int) ChronoUnit.DAYS.between(start, end);
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid date format. Please use YYYY-MM-DD.", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        runInternalTests();
    }

    private void runInternalTests() {
        Log.d("TEST", "Running internal tests...");
        testInvalidDateInput();
        testEmptyLocationInput();
        testNavigationButtons();
        Log.d("TEST", "Internal tests completed.");
    }

    private void testInvalidDateInput() {
        int duration = calculateTravelDuration("invalid-date", "2024-10-10");
        Log.d("TEST", "Invalid Date Test: " + (duration == 0 ? "PASSED" : "FAILED"));
    }

    private void testEmptyLocationInput() {
        locationInput.setText("");
        calculateVacationButton.performClick();
        Log.d("TEST", "Empty Location Test: " + ("Please enter a location".equals(locationInput.getText().toString()) ? "PASSED" : "FAILED"));
    }

    private void testNavigationButtons() {
        try {
            findViewById(R.id.button).performClick();
            findViewById(R.id.button3).performClick();
            Log.d("TEST", "Navigation Buttons Test: PASSED");
        } catch (Exception e) {
            Log.d("TEST", "Navigation Buttons Test FAILED with exception: " + e.getMessage());
        }
    }
}
