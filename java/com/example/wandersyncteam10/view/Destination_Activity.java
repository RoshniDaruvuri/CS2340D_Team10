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
import java.util.stream.Collectors;


public class Destination_Activity extends AppCompatActivity {

    private LinearLayout formLayout;
    private EditText locationInput, startDateInput, endDateInput;
    private ListView travelLogsList;

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

        Button logTravelButton = findViewById(R.id.log_travel_button);
        Button calculateVacationButton = findViewById(R.id.calculate_vacation_button);
        formLayout.setVisibility(View.GONE);

        updateTravelLogsList();

        // show the form when "Log Travel" is clicked
        logTravelButton.setOnClickListener(v -> formLayout.setVisibility(View.VISIBLE));
        //updateTravelLogsList();




        // Initialize "Calculate Vacation Time" button and Travel Location EditText
        Button calculateVacationTimeButton = findViewById(R.id.calculate_vacation_button);
        //EditText travelLocationInput = findViewById(R.id.travel_location_input);

        // Handle form submission on "Calculate Vacation Time"
        calculateVacationButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            String startDate = startDateInput.getText().toString();
            String endDate = endDateInput.getText().toString();

            // Validate form input
            if (location.isEmpty()) {
                Toast.makeText(Destination_Activity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                return;
            }

            //if (!isValidDate(startDate) || !isValidDate(endDate)) {
            //Toast.makeText(Destination_Activity.this, "Please enter valid dates", Toast.LENGTH_SHORT).show();
            //return;
            //}

            //if (!isEndDateValid(startDate, endDate)) {
            //    Toast.makeText(Destination_Activity.this, "End date must be after start date", Toast.LENGTH_SHORT).show();
            //    return;
            //}

            // Save travel data in the Singleton Database
            DestinationDatabase.getInstance(Destination_Activity.this).addTravelLog(location, startDate, endDate);
            Toast.makeText(Destination_Activity.this, "Vacation logged successfully!", Toast.LENGTH_SHORT).show();

            // Update the travel logs list
            updateTravelLogsList();

            // Optionally hide the form after logging
            formLayout.setVisibility(View.GONE);
        });





// DASHBOARD BUTTONS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Destination_Activity.this, Logistics_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Destination_Activity.this, Destination_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Destination_Activity.this, Dining_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Destination_Activity.this, Accommodations_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Destination_Activity.this, Transportation_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Destination_Activity.this, Travel_Activity.class);
                startActivity(intent);
            }
        });
    }



    private void updateTravelLogsList() {
        // Fetch the travel logs from the Singleton Database
        DestinationDatabase db = DestinationDatabase.getInstance(this);
        List<TravelLog> travelLogs = db.getTravelLogs();  // Get the travel logs

        if (travelLogs.isEmpty()) {
            Log.d("DEBUG", "No travel logs found in database");
        } else {
            Log.d("DEBUG", "Found " + travelLogs.size() + " travel logs.");
        }

        // Prepare a list of strings to display (formatted with location and duration)
        List<String> travelLogStrings = new ArrayList<>();
        for (TravelLog log : travelLogs) {
            String duration = getTravelDuration(log.getStartDate(), log.getEndDate()) + " days";
            travelLogStrings.add(log.getLocation() + " (" + duration + ")");
        }

        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, travelLogStrings);
        travelLogsList.setAdapter(adapter);
    }

    private int getTravelDuration(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);  // Parse the start date
            LocalDate end = LocalDate.parse(endDate);      // Parse the end date
            return (int) ChronoUnit.DAYS.between(start, end);  // Calculate duration in days
        } catch (DateTimeParseException e) {
            return 0;  // If dates are invalid, return 0
        }
    }
}
