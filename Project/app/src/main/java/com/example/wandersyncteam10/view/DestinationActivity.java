package com.example.wandersyncteam10.view;

import com.example.wandersyncteam10.Model.DestinationDatabase;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        addedUser = findViewById(R.id.text_invite);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            travelLogsRef = FirebaseDatabase.getInstance().getReference("travelLogs").child(currentUser.getUid());
            updateTravelLogsList();
            updateTotalVacationDays();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

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
            addedUser.setVisibility(View.VISIBLE);

            // Clear input fields
            locationInput.setText("");
            startDateInput.setText("");
            endDateInput.setText("");
            addedUser.setText("");
        });

        // Handle form submission on "Calculate Vacation Time"
        calculateVacationButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            String startDate = startDateInput.getText().toString();
            String endDate = endDateInput.getText().toString();
            String invitedUser = addedUser.getText().toString();

            if (location.isEmpty()) {
                Toast.makeText(DestinationActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(DestinationActivity.this, "Please enter both start and end dates",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
                Toast.makeText(DestinationActivity.this, "Invalid date format. Please use YYYY-MM-DD.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            DestinationDatabase.getInstance(DestinationActivity.this).addTravelLog(location, startDate, endDate, invitedUser);

            Toast.makeText(DestinationActivity.this, "Vacation logged successfully!",
                    Toast.LENGTH_SHORT).show();

            saveTravelLog(location, startDate, endDate, invitedUser);
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
                Toast.makeText(DestinationActivity.this, "Please enter both start and end dates",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = calculateTravelDuration(startDateInputText, endDateInputText);
            durationOutcome.setText(duration + " days");

            // Save the calculated duration to the database
            saveCalculatedDuration(duration);
        });

        // Dashboard button listeners (keeping these unchanged)
        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button2).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, DestinationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button4).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, AccommodationsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button5).setOnClickListener(view -> {
            Intent intent = new Intent(DestinationActivity.this, CommunityActivity.class);
            startActivity(intent);
        });

//        findViewById(R.id.button6).setOnClickListener(view -> {
//            Intent intent = new Intent(DestinationActivity.this, TravelActivity.class);
//            startActivity(intent);
//        });
    }


    /**
     * updates total vacation days
     * */
    private void updateTotalVacationDays() {
        DestinationDatabase db = DestinationDatabase.getInstance(this);
        int totalDays = db.getTotalVacationDays();
        totalVacationDaysView.setText("Result: " + totalDays + " days");


    }

    /**
     * updates travel logs list
     * */
    private void updateTravelLogsList() {
        if (currentUser == null) {
            return;
        }

        travelLogsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> travelLogStrings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String location = snapshot.child("location").getValue(String.class);
                    String startDate = snapshot.child("startDate").getValue(String.class);
                    String invitedUser = snapshot.child("invitedUser").getValue(String.class);
                    String endDate = snapshot.child("endDate").getValue(String.class);
                    int duration = calculateTravelDuration(startDate, endDate);
                    travelLogStrings.add(location + " (" + duration + " days)" + " Invited User: " + invitedUser);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(DestinationActivity.this,
                        android.R.layout.simple_list_item_1, travelLogStrings);
                travelLogsList.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseTravelLogs", "Failed to load logs", error.toException());
            }
        });
    }
    private void saveTripToFirestore(String destination, String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a map of the trip details
        Map<String, Object> tripDetails = new HashMap<>();
        tripDetails.put("destination", destination);
        tripDetails.put("date", date);
        tripDetails.put("createdBy", FirebaseAuth.getInstance().getCurrentUser().getEmail());

        // Save to Firestore
        db.collection("trips")
                .add(tripDetails)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(DestinationActivity.this, "Trip Added", Toast.LENGTH_SHORT).show();
                    // Optionally, you can add contributors here after trip is added
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DestinationActivity.this, "Failed to add trip", Toast.LENGTH_SHORT).show();
                });
    }


    /**
     * @param location    location
     * @param startDate   start date
     * @param endDate     end date
     *                    saves travel log
     * @param invitedUser
     */
    private void saveTravelLog(String location, String startDate, String endDate, String invitedUser) {
        if (currentUser == null) {
            return;
        }

        String key = travelLogsRef.push().getKey(); // Ensure you're only pushing to user-specific logs
        if (key != null) {
            HashMap<String, String> travelData = new HashMap<>();
            travelData.put("location", location);
            travelData.put("startDate", startDate);
            travelData.put("endDate", endDate);

            // Get the invited user's name from the EditText (addedUser)
            invitedUser = addedUser.getText().toString();

            // Check if the invited user name is not empty
            if (!invitedUser.isEmpty()) {
                travelData.put("invitedUser", invitedUser); // Save the invited user's name
            } else {
                travelData.put("invitedUser", ""); // If no user is added, set it to empty string
            }

            travelLogsRef.child(key).setValue(travelData); // Save to Firebase
        }
    }


    /**
     * @param duration calculated duration
     * saves calculated duration to database
     * */
    private void saveCalculatedDuration(int duration) {
        // Save duration to Firebase or local DB
        // You may need to implement a new database structure for this
        // This function is a placeholder
    }

    /**
     * @param startDate start date
     * @param endDate end date
     * @return number of days between start and end date
     * */
    private int calculateTravelDuration(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return (int) ChronoUnit.DAYS.between(start, end);
        } catch (DateTimeParseException e) {
            Toast.makeText(DestinationActivity.this, "Invalid date format. Please use YYYY-MM-DD.",
                    Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    /**
     * @param date date to validate
     * @return true if date is in valid format
     * */
    private boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

