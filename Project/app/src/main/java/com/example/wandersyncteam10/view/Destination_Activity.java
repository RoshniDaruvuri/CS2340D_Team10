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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersyncteam10.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Destination_Activity extends AppCompatActivity {

    private LinearLayout formLayout;
    private EditText locationInput, startDateInput, endDateInput, startInput, endInput;
    private ListView travelLogsList;
    private TextView totalVacationDaysView, durationOutcome;
    private Button calculateDurationButton, calculateButton;

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

        // Initialize views
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

        // Hide extra inputs initially
        startInput.setVisibility(View.GONE);
        endInput.setVisibility(View.GONE);
        durationOutcome.setVisibility(View.GONE);
        calculateButton.setVisibility(View.GONE);

        updateTravelLogsList();
        updateTotalVacationDays();

        Button logTravelButton = findViewById(R.id.log_travel_button);
        Button calculateVacationButton = findViewById(R.id.calculate_vacation_button);
        formLayout.setVisibility(View.GONE);

        // Show the form when "Log Travel" is clicked
        logTravelButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            calculateDurationButton.setVisibility(View.GONE);
            startInput.setVisibility(View.GONE);
            endInput.setVisibility(View.GONE);
            durationOutcome.setVisibility(View.GONE);
            calculateButton.setVisibility(View.GONE);
        });

        // Handle form submission on "Calculate Vacation Time"
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
            updateTotalVacationDays();
            formLayout.setVisibility(View.GONE);
            calculateDurationButton.setVisibility(View.VISIBLE);
        });

        // Show duration input fields when "Calculate Duration" is clicked
        calculateDurationButton.setOnClickListener(v -> {
            startInput.setVisibility(View.VISIBLE);
            endInput.setVisibility(View.VISIBLE);
            durationOutcome.setVisibility(View.VISIBLE);
            calculateButton.setVisibility(View.VISIBLE);
        });

        // Calculate duration based on start and end date inputs
        calculateButton.setOnClickListener(v -> {
            String startDateInputText = startInput.getText().toString();
            String endDateInputText = endInput.getText().toString();

            if (startDateInputText.isEmpty() || endDateInputText.isEmpty()) {
                Toast.makeText(Destination_Activity.this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = calculateTravelDuration(startDateInputText, endDateInputText);
            durationOutcome.setText(duration + " days");

            // Save the calculated duration to the database
            saveCalculatedDuration(duration);
        });

        // Dashboard button listeners (keeping these unchanged)
        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent(Destination_Activity.this, Logistics_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button2).setOnClickListener(view -> {
            Intent intent = new Intent(Destination_Activity.this, Destination_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent intent = new Intent(Destination_Activity.this, Dining_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button4).setOnClickListener(view -> {
            Intent intent = new Intent(Destination_Activity.this, Accommodations_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button5).setOnClickListener(view -> {
            Intent intent = new Intent(Destination_Activity.this, Transportation_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button6).setOnClickListener(view -> {
            Intent intent = new Intent(Destination_Activity.this, Travel_Activity.class);
            startActivity(intent);
        });
    }

    private void updateTotalVacationDays() {
        DestinationDatabase db = DestinationDatabase.getInstance(this);
        int totalDays = db.getTotalVacationDays();
        totalVacationDaysView.setText("Result: " + totalDays + " days");
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

    public static int getTravelDuration(String startDate, String endDate) {
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

    private void saveCalculatedDuration(int duration) {
        // Create a unique key for each duration entry (e.g., using push to generate a unique ID)
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("calculatedDurations");
        String key = databaseRef.push().getKey(); // Generate a new unique key

        if (key != null) {
            // Create a map or a custom object for storing the duration
            HashMap<String, Object> durationData = new HashMap<>();
            durationData.put("duration", duration);

            // Save the duration data
            databaseRef.child(key).setValue(durationData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Destination_Activity.this, "Duration saved successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Destination_Activity.this, "Failed to save duration.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadCalculatedDurations() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("calculatedDurations");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer duration = snapshot.child("duration").getValue(Integer.class);
                    // Do something with the loaded duration (e.g., display in a list)
                    Log.d("FirebaseDuration", "Loaded duration: " + duration);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseDuration", "Failed to load durations", databaseError.toException());
            }
        });
    }
}
