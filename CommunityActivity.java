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

    private EditText startDate, endDate, destination, accommodation, dining, transportation, notes;
    private Button submitPostButton, viewTripsButton;

    private FirebaseUser currentUser;
    private DatabaseReference travelPostsRef;
    private TravelPostManager travelPostManager; // Added TravelPostManager reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);


        // Initialize the UI components
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

        // Initialize TravelPostManager
        travelPostManager = TravelPostManager.getInstance(); // Added initialization

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

        // Button click to view submitted trips
        viewTripsButton.setOnClickListener(v -> {
            // Navigate to ViewTripsActivity to display submitted trips
            Intent intent = new Intent(CommunityActivity.this, ViewTripsActivity.class);
            startActivity(intent);
        });

    }

    private void submitTravelPost() {
        String startDateValue = startDate.getText().toString();
        String endDateValue = endDate.getText().toString();
        String destinationValue = destination.getText().toString();
        String accommodationValue = accommodation.getText().toString();
        String diningValue = dining.getText().toString();
        String transportationValue = transportation.getText().toString();
        String notesValue = notes.getText().toString();

        // Validate the input data
        if (validateInput(startDateValue, endDateValue, destinationValue, accommodationValue, diningValue, transportationValue)) {
            TravelPost post = new TravelPost(startDateValue, endDateValue, destinationValue, accommodationValue, diningValue, transportationValue, notesValue);

            // Add to Firebase
            String postId = travelPostsRef.push().getKey();
            if (postId != null) {
                travelPostsRef.child(postId).setValue(post);
                Toast.makeText(this, "Post submitted successfully!", Toast.LENGTH_SHORT).show();
                clearInputs();
            }

            // Notify TravelPostManager
            travelPostManager.addTravelPost(post); // Notify observers
        } else {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
        }
    }

    // validate that all fields are filled in
    private boolean validateInput(String startDate, String endDate, String destination, String accommodation, String dining, String transportation) {
        return !startDate.isEmpty() && !endDate.isEmpty() && !destination.isEmpty() && !accommodation.isEmpty() && !dining.isEmpty() && !transportation.isEmpty();
    }

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
