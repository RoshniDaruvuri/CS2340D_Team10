package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wandersyncteam10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommunityActivity extends AppCompatActivity {

    private EditText startDate;
    private EditText endDate;
    private EditText destination;
    private EditText accommodation;
    private EditText dining;
    private EditText transportation;
    private EditText notes;
    private Button submitPostButton;
    private Button viewTripsButton;

    private FirebaseUser currentUser;
    private DatabaseReference travelPostsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // Initialize UI components
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        destination = findViewById(R.id.destination);
        accommodation = findViewById(R.id.accommodation);
        dining = findViewById(R.id.dining);
        transportation = findViewById(R.id.transportation);
        notes = findViewById(R.id.notes);
        submitPostButton = findViewById(R.id.submitPostButton);
        viewTripsButton = findViewById(R.id.viewTripsButton);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(CommunityActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Set up reference for travel posts
        if (currentUser != null) {
            travelPostsRef = FirebaseDatabase.getInstance()
                    .getReference("communityPosts")
                    .child(currentUser.getUid());
        }

        // Submit travel post logic
        submitPostButton.setOnClickListener(v -> submitTravelPost());

        // Button click to view submitted trips
        viewTripsButton.setOnClickListener(v -> {
            // Navigate to ViewTripsActivity to display submitted trips
            Intent intent = new Intent(CommunityActivity.this, ViewTripsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent(CommunityActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button2).setOnClickListener(view -> {
            Intent intent = new Intent(CommunityActivity.this, DestinationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent intent = new Intent(CommunityActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button4).setOnClickListener(view -> {
            Intent intent = new Intent(CommunityActivity.this, AccommodationsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button5).setOnClickListener(view -> {
            Intent intent = new Intent(CommunityActivity.this, CommunityActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button6).setOnClickListener(view -> {
            Intent intent = new Intent(CommunityActivity.this, TravelActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Submits a new travel post.
     */
    private void submitTravelPost() {
        String startDateValue = startDate.getText().toString();
        String endDateValue = endDate.getText().toString();
        String destinationValue = destination.getText().toString();
        String accommodationValue = accommodation.getText().toString();
        String diningValue = dining.getText().toString();
        String transportationValue = transportation.getText().toString();
        String notesValue = notes.getText().toString();

        if (validateInput(startDateValue, endDateValue, destinationValue, accommodationValue, diningValue, transportationValue)) {
            String commonId = travelPostsRef.push().getKey(); // Generate unique ID

            TravelPost post = new TravelPost(commonId, startDateValue, endDateValue, destinationValue, accommodationValue, diningValue, transportationValue, notesValue);

            // Save to communityPosts database
            if (commonId != null) {
                travelPostsRef.child(commonId).setValue(post);
            }

            // Save to travelLogs database
            DatabaseReference travelLogsRef = FirebaseDatabase.getInstance()
                    .getReference("travelLogs")
                    .child(currentUser.getUid())
                    .child(commonId);
            travelLogsRef.setValue(post);

            Toast.makeText(this, "Post submitted successfully!", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Validates the input fields for the travel post.
     *
     * @param startDate       The start date of the trip.
     * @param endDate         The end date of the trip.
     * @param destination     The trip destination.
     * @param accommodation   Accommodation details.
     * @param dining          Dining details.
     * @param transportation  Transportation details.
     * @return True if all fields are valid, false otherwise.
     */
    private boolean validateInput(String startDate, String endDate,
                                  String destination, String accommodation,
                                  String dining, String transportation) {
        return !startDate.isEmpty() && !endDate.isEmpty()
                && !destination.isEmpty() && !accommodation.isEmpty()
                && !dining.isEmpty() && !transportation.isEmpty();
    }

    /**
     * Clears all input fields after submitting a travel post.
     */
    private void clearInputs() {
        startDate.setText("");
        endDate.setText("");
        destination.setText("");
        accommodation.setText("");
        dining.setText("");
        transportation.setText("");
        notes.setText("");
    }
}
