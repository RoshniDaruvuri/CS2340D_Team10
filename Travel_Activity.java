package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersyncteam10.R;

public class Travel_Activity extends AppCompatActivity {

    private EditText editTextLocation;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private EditText editTextDuration;
    private Button buttonSaveLog;
    private TravelLogManager travelLogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_travel);

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize TravelLogManager
        travelLogManager = new TravelLogManager();

        // Initialize input fields
        editTextLocation = findViewById(R.id.location_input);
        editTextStartDate = findViewById(R.id.start_date_input);
        editTextEndDate = findViewById(R.id.end_date_input);
        editTextDuration = findViewById(R.id.duration_outcome); // Make sure this EditText is editable if needed
        buttonSaveLog = findViewById(R.id.calculate_vacation_button); // Change this to a button that makes sense for your logic

        // Set up save log button listener
        buttonSaveLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editTextLocation.getText().toString().trim();
                String startDate = editTextStartDate.getText().toString().trim();
                String endDate = editTextEndDate.getText().toString().trim();
                String durationString = editTextDuration.getText().toString().trim();

                if (TextUtils.isEmpty(location) || TextUtils.isEmpty(startDate) ||
                        TextUtils.isEmpty(endDate) || TextUtils.isEmpty(durationString)) {
                    Toast.makeText(Travel_Activity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    int duration = Integer.parseInt(durationString); // Convert duration to int
                    travelLogManager.saveTravelLog(location, startDate, endDate, duration);
                    Toast.makeText(Travel_Activity.this, "Travel log saved!", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                }
            }
        });

        // Existing button listeners for navigation
        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent(Travel_Activity.this, Logistics_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button2).setOnClickListener(view -> {
            Intent intent = new Intent(Travel_Activity.this, Destination_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent intent = new Intent(Travel_Activity.this, Dining_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button4).setOnClickListener(view -> {
            Intent intent = new Intent(Travel_Activity.this, Accommodations_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button5).setOnClickListener(view -> {
            Intent intent = new Intent(Travel_Activity.this, Transportation_Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.button6).setOnClickListener(view -> {
            Intent intent = new Intent(Travel_Activity.this, Travel_Activity.class);
            startActivity(intent);
        });
    }

    private void clearInputFields() {
        editTextLocation.setText("");
        editTextStartDate.setText("");
        editTextEndDate.setText("");
        editTextDuration.setText("");
    }
}

