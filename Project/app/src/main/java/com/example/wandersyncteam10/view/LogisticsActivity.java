package com.example.wandersyncteam10.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersyncteam10.Model.TravelLog;
import com.example.wandersyncteam10.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity {

    private TextView usernameDisplay;
    private List<String> contributorsList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);

        // Initialize Firestore.
        db = FirebaseFirestore.getInstance();

        // Find the TextView for displaying usernames.
        usernameDisplay = findViewById(R.id.username_display);

        // Initialize the list of contributors.
        contributorsList = new ArrayList<>();

        // Set the insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load contributors from Firestore when the activity starts
        loadContributorsFromFirestore();

        // Handle Add Contributors button click
        findViewById(R.id.add_contributors_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddContributorDialog();
            }
        });
        // Find the button by its ID
        Button viewTripsButton = findViewById(R.id.view_trips_button);
        // Set an OnClickListener for the button
        viewTripsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to TripsActivity
                Intent intent = new Intent(LogisticsActivity.this, DestinationActivity.class);

                // Optional: Log the button click for debugging
                Log.d("LogisticsActivity", "View Trips button clicked");

                // Start the TripsActivity
                startActivity(intent);
            }
        });

        // Other buttons remain unchanged...
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogisticsActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.graph_button).setOnClickListener((l) -> draw());

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogisticsActivity.this, DestinationActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogisticsActivity.this, DiningActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogisticsActivity.this, AccommodationsActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogisticsActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });
//        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LogisticsActivity.this, TravelActivity.class);
//                startActivity(intent);
//            }
//        });

        findViewById(R.id.notesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogisticsActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Displays a dialog to add a contributor to the list.
     */
    private void showAddContributorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Contributor");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contributor, null);
        builder.setView(dialogView);

        final EditText input = dialogView.findViewById(R.id.username_input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = input.getText().toString();
                if (!username.isEmpty()) {
                    addContributorToFirestore(username);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Loads the list of contributors from Firestore and updates the display.
     */
    private void loadContributorsFromFirestore() {
        // Reference the Firestore collection (e.g., "contributors")
        CollectionReference contributorsRef = db.collection("contributors");

        contributorsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    contributorsList.clear(); // Clear the current list
                    for (DocumentSnapshot document : task.getResult()) {
                        String contributor = document.getString("name");
                        contributorsList.add(contributor);
                    }
                    // Update the display after loading from Firestore
                    updateContributorsDisplay();
                } else {
                    Log.w("Firestore", "Error getting documents.", task.getException());
                }
            }
        });
    }

    /**
     * Adds a contributor to Firestore and updates the local list of contributors.
     *
     * @param username The username of the contributor to be added.
     */
    private void addContributorToFirestore(String username) {
        // Add the new contributor to Firestore
        db.collection("contributors").add(new Contributor(username))
                .addOnSuccessListener(documentReference -> {
                    // Add the new contributor to the list and update the display
                    contributorsList.add(username);
                    updateContributorsDisplay();
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
    }

    /**
     * Updates the display of contributors in the TextView.
     */
    private void updateContributorsDisplay() {
        StringBuilder contributorsText = new StringBuilder("Contributors:\n");
        for (String contributor : contributorsList) {
            contributorsText.append(contributor).append("\n");
        }
        usernameDisplay.setText(contributorsText.toString());
    }

    /**
     * draw
     * this method creates the bar chart for the actual total duration of the trips vs the alloted duration
     * we reference firebase and the destinationDatabase and the travelLogManager to do so
     *
     * */

    public void draw() {

        // Reference to the Firebase database for travelLogs
        DatabaseReference travelLogsRef = FirebaseDatabase.getInstance().getReference("travelLogs");
        DatabaseReference calculatedDurationRef = FirebaseDatabase.getInstance().getReference("calculatedDuration");

        // Initialize total duration variables
        final float[] totalDuration = {0}; // To store the total from travelLogs
        final float[] calculatedTotalDuration = {0}; // To store the total from calculatedDuration

        // First, retrieve and sum the total duration from travelLogs
        travelLogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot travelLogsSnapshot) {
                // Loop through each travel log in Firebase
                for (DataSnapshot snapshot : travelLogsSnapshot.getChildren()) {
                    TravelLog travelLog = snapshot.getValue(TravelLog.class);
                    if (travelLog != null) {
                        totalDuration[0] += travelLog.getDuration();
                    }
                }

                // Now, retrieve and sum the total duration from calculatedDuration
                calculatedDurationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot calculatedDurationSnapshot) {
                        // Loop through each entry in calculatedDuration
                        for (DataSnapshot snapshot : calculatedDurationSnapshot.getChildren()) {
                            // Assuming each entry has a field named "duration"
                            Long duration = snapshot.child("duration").getValue(Long.class);
                            // Change to Double if needed
                            if (duration != null) {
                                calculatedTotalDuration[0] += duration; // Sum up the duration
                            }
                        }

                        // Create the BarEntry with the total durations
                        List<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0f, totalDuration[0])); // Entry for totalDuration
                        entries.add(new BarEntry(1f, calculatedTotalDuration[0])); // Entry for calculatedTotalDuration

                        // Set up the BarDataSet
                        BarDataSet dataSet = new BarDataSet(entries, "Duration Data");
                        dataSet.setColors(new int[]{Color.BLUE, Color.GREEN}); // Set different colors for each bar
                        dataSet.setValueTextColor(Color.WHITE);
                        dataSet.setValueTextSize(10f);

                        BarData barData = new BarData(dataSet);
                        BarChart barChart = findViewById(R.id.BarChart);
                        barChart.setData(barData);

                        // Refresh the chart
                        barChart.invalidate(); // Redraw the chart
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("FirebaseError", "Failed to load calculated durations: " + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to load travel logs: " + databaseError.getMessage());
            }
        });
    }


    /**
     * Represents a contributor in the application.
     * This class holds the information about a contributor, including their name.
     */
    public class Contributor {

        /**
         * The name of the contributor.
         */
        private String name;

        /**
         * Constructs a new Contributor with the specified name.
         *
         * @param name the name of the contributor
         */
        public Contributor(String name) {
            this.name = name;
        }

        /**
         * Returns the name of the contributor.
         *
         * @return the name of the contributor
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the contributor.
         *
         * @param name the new name for the contributor
         */
        public void setName(String name) {
            this.name = name;
        }


    }

}
