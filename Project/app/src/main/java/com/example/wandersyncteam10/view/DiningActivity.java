package com.example.wandersyncteam10.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wandersyncteam10.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

// Observer interface
interface ReservationObserver {
    void onReservationUpdated(List<Reservation> reservations);
}

// Subject class to manage observers
class ReservationSubject {
    private List<ReservationObserver> observers = new ArrayList<>();

    void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }

    void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    void notifyObservers(List<Reservation> reservations) {
        for (ReservationObserver observer : observers) {
            observer.onReservationUpdated(reservations);
        }
    }
}

public class DiningActivity extends AppCompatActivity implements ReservationObserver {

    private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm";
    private TextView diningDisplay;
    private List<Reservation> reservationList;
    private FirebaseFirestore db;
    private ReservationSubject reservationSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining);

        db = FirebaseFirestore.getInstance();
        reservationSubject = new ReservationSubject();
        reservationSubject.addObserver(this);

        // Find the TextView for displaying reservations
        diningDisplay = findViewById(R.id.dining_display);

        // Initialize the list of reservations
        reservationList = new ArrayList<>();

        // Load reservations from Firestore when the activity starts
        loadReservationsFromFirestore();

        // Handle Add Reservation button click
        findViewById(R.id.buttonAddReservation).setOnClickListener(view -> showAddReservationDialog());

        // Handle Sort button click
        findViewById(R.id.buttonSortReservations).setOnClickListener(view -> sortReservationsByDateTime());

        // Dashboard button listeners (keeping these unchanged)
        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent(DiningActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button2).setOnClickListener(view -> {
            Intent intent = new Intent(DiningActivity.this, DestinationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent intent = new Intent(DiningActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button4).setOnClickListener(view -> {
            Intent intent = new Intent(DiningActivity.this, AccommodationsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button5).setOnClickListener(view -> {
            Intent intent = new Intent(DiningActivity.this, CommunityActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button6).setOnClickListener(view -> {
            Intent intent = new Intent(DiningActivity.this, TravelActivity.class);
            startActivity(intent);
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

        builder.setPositiveButton("Add", (dialog, which) -> {
            String location = locationInput.getText().toString();
            String website = websiteInput.getText().toString();
            String time = timeInput.getText().toString();
            if (!location.isEmpty() && !website.isEmpty()
                    && isValidTimeFormat(time) && !isDuplicateReservation(location, time)) {
                addReservationToFirestore(new Reservation(location, website, time));
            } else {
                String message = (!isValidTimeFormat(time)) ? "Invalid time format. Use "
                        + DATE_FORMAT : "Duplicate reservation.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Validates that the time string is in the correct format.
     */
    private boolean isValidTimeFormat(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setLenient(false);
            dateFormat.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Checks if a reservation with the same location and time already exists.
     */
    private boolean isDuplicateReservation(String location, String time) {
        for (Reservation reservation : reservationList) {
            if (reservation.getLocation().equalsIgnoreCase(location) && reservation.getTime().equals(time)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Loads the list of reservations from Firestore and updates the display.
     */
    private void loadReservationsFromFirestore() {
        CollectionReference reservationRef = db.collection("reservations");

        reservationRef.get().addOnCompleteListener(task -> {
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
                reservationSubject.notifyObservers(reservationList);
            } else {
                Log.w("Firestore", "Error getting documents.", task.getException());
            }
        });
    }

    /**
     * Adds a reservation to Firestore and updates the local list of reservations.
     */
    private void addReservationToFirestore(Reservation reservation) {
        db.collection("reservations").add(reservation)
                .addOnSuccessListener(documentReference -> {
                    reservationList.add(reservation);
                    updateDiningDisplay();
                    reservationSubject.notifyObservers(reservationList);
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
    }

    /**
     * Updates the display of reservations in the TextView.
     */
    private void updateDiningDisplay() {
        StringBuilder displayText = new StringBuilder("Reservations:\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date currentDate = new Date();

        for (Reservation reservation : reservationList) {
            try {
                Date reservationDate = dateFormat.parse(reservation.getTime());
                String color = reservationDate != null && reservationDate.before(currentDate) ? "#FF0000" : "#00FF00";
                displayText.append("<font color='").append(color).append("'>")
                        .append(reservation.getLocation()).append(" - ")
                        .append(reservation.getWebsite()).append(" at ")
                        .append(reservation.getTime()).append("</font><br>");
            } catch (ParseException e) {
                Log.w("DiningActivity", "Error parsing date format", e);
            }
        }

        diningDisplay.setText(Html.fromHtml(displayText.toString()));
    }

    /**
     * Sorts reservations by their time.
     */
    private void sortReservationsByDateTime() {
        Collections.sort(reservationList, Comparator.comparing(Reservation::getTime));
        reservationSubject.notifyObservers(reservationList);
    }

    @Override
    public void onReservationUpdated(List<Reservation> reservations) {
        updateDiningDisplay();
    }

}




