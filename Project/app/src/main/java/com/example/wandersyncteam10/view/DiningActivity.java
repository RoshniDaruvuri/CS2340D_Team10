package com.example.wandersyncteam10.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersyncteam10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiningActivity extends AppCompatActivity {

    private TextView diningDisplay;
    private List<Reservation> reservationList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Find the TextView for displaying reservations
        diningDisplay = findViewById(R.id.dining_display);

        // Initialize the list of reservations
        reservationList = new ArrayList<>();

        // Set the insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load reservations from Firestore when the activity starts
        loadReservationsFromFirestore();

        // Handle Add Reservation button click
        findViewById(R.id.buttonAddReservation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddReservationDialog();
            }
        });
    }

    /**
     * Displays a dialog to add a reservation.
     */
    private void showAddReservationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Reservation");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_dining, null);
        builder.setView(dialogView);

        final EditText locationInput = dialogView.findViewById(R.id.editTextLocation);
        final EditText websiteInput = dialogView.findViewById(R.id.editTextWebsite);
        final EditText timeInput = dialogView.findViewById(R.id.editTextTime);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String location = locationInput.getText().toString();
                String website = websiteInput.getText().toString();
                String time = timeInput.getText().toString();
                if (!location.isEmpty() && !website.isEmpty() && !time.isEmpty()) {
                    addReservationToFirestore(new Reservation(location, website, time));
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
     * Loads the list of reservations from Firestore and updates the display.
     */
    private void loadReservationsFromFirestore() {
        CollectionReference reservationRef = db.collection("reservations");

        reservationRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    reservationList.clear(); // Clear the current list
                    for (DocumentSnapshot document : task.getResult()) {
                        String location = document.getString("location");
                        String website = document.getString("website");
                        String time = document.getString("time");
                        reservationList.add(new Reservation(location, website, time));
                    }
                    // Update the display after loading from Firestore
                    updateDiningDisplay();
                } else {
                    Log.w("Firestore", "Error getting documents.", task.getException());
                }
            }
        });
    }

    /**
     * Adds a reservation to Firestore and updates the local list of reservations.
     * @param reservation The reservation to be added.
     */
    private void addReservationToFirestore(Reservation reservation) {
        db.collection("reservations").add(reservation)
                .addOnSuccessListener(documentReference -> {
                    reservationList.add(reservation);
                    updateDiningDisplay();
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
    }

    /**
     * Updates the display of reservations in the TextView.
     */
    private void updateDiningDisplay() {
        StringBuilder displayText = new StringBuilder("Reservations:\n");
        for (Reservation reservation : reservationList) {
            displayText.append(reservation.getLocation()).append(" - ")
                    .append(reservation.getWebsite()).append(" at ")
                    .append(reservation.getTime()).append("\n");
        }
        diningDisplay.setText(displayText.toString());
    }
}