package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private EditText startDate, endDate, destination, accommodation, dining, transportation, notes;
    private Button submitPostButton;
    private ListView travelPostsListView;

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
        travelPostsListView = findViewById(R.id.travelPostsListView);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // User is signed out, redirect to LoginActivity
                Intent intent = new Intent(CommunityActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };


        if (currentUser == null) {
            Intent intent = new Intent(CommunityActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Prevent the user from coming back to this activity without logging in.
        }

        // Check if the user is logged in
        if (currentUser != null) {
            travelPostsRef = FirebaseDatabase.getInstance()
                    .getReference("communityPosts")
                    .child(currentUser.getUid());
            setupDatabaseListener();
        } else {
            Toast.makeText(this, "User not logged in. Please log in to continue.", Toast.LENGTH_SHORT).show();
            Log.e("CommunityActivity", "User is null");
        }

        // Set up listeners for buttons
        submitPostButton.setOnClickListener(v -> submitTravelPost());

        // Dashboard button listeners (keeping these unchanged)
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
            travelPostsRef.push().setValue(post)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Travel post created successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to create travel post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("CommunityActivity", "Error: " + e.getMessage());
                    });
        }
    }

    private boolean validateInput(String startDate, String endDate, String destination, String accommodation, String dining, String transportation) {
        // Simple validation logic (expand as needed)
        if (startDate.isEmpty() || endDate.isEmpty() || destination.isEmpty() || accommodation.isEmpty() || dining.isEmpty() || transportation.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Add date validation if needed (e.g., date format, logical order)
        return true;
    }

    private void setupDatabaseListener() {
        travelPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TravelPost> posts = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TravelPost post = data.getValue(TravelPost.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }

                List<String> formattedPosts = new ArrayList<>();
                for (TravelPost post : posts) {
                    String postDetails = "Destination: " + post.getDestination() + "\n" +
                            "Start Date: " + post.getStartDate() + "\n" +
                            "End Date: " + post.getEndDate() + "\n" +
                            "Accommodation: " + post.getAccommodation() + "\n" +
                            "Dining: " + post.getDining() + "\n" +
                            "Transportation: " + post.getTransportation() + "\n" +
                            "Notes: " + post.getNotes();
                    formattedPosts.add(postDetails);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CommunityActivity.this, android.R.layout.simple_list_item_1, formattedPosts);
                travelPostsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommunityActivity.this, "Failed to load travel posts: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CommunityActivity", "Error: " + error.getMessage());
            }
        });
    }
}

