package com.example.wandersyncteam10.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
<<<<<<< HEAD
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
=======
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
>>>>>>> origin/main

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

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

public class DiningActivity extends AppCompatActivity {
=======
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
>>>>>>> origin/main

    private TextView diningDisplay;
    private List<Reservation> reservationList;
    private FirebaseFirestore db;
<<<<<<< HEAD
=======
    private ReservationSubject reservationSubject;
>>>>>>> origin/main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining);

<<<<<<< HEAD
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
=======
        // Initialize Firestore and subject
        db = FirebaseFirestore.getInstance();
        reservationSubject = new ReservationSubject();
        reservationSubject.addObserver(this);
>>>>>>> origin/main

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
<<<<<<< HEAD
        findViewById(R.id.buttonAddReservation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddReservationDialog();
            }
        });
=======
        findViewById(R.id.buttonAddReservation).setOnClickListener(view -> showAddReservationDialog());

        // Handle Sort button click
        findViewById(R.id.buttonSortReservations).setOnClickListener(view -> sortReservationsByDateTime());
>>>>>>> origin/main
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

<<<<<<< HEAD
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

=======
        builder.setPositiveButton("Add", (dialog, which) -> {
            String location = locationInput.getText().toString();
            String website = websiteInput.getText().toString();
            String time = timeInput.getText().toString();
            if (!location.isEmpty() && !website.isEmpty() && isValidTimeFormat(time) && !isDuplicateReservation(location, time)) {
                addReservationToFirestore(new Reservation(location, website, time));
            } else {
                String message = (!isValidTimeFormat(time)) ? "Invalid time format. Use " + DATE_FORMAT : "Duplicate reservation.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
>>>>>>> origin/main
        builder.show();
    }

    /**
<<<<<<< HEAD
=======
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
>>>>>>> origin/main
     * Loads the list of reservations from Firestore and updates the display.
     */
    private void loadReservationsFromFirestore() {
        CollectionReference reservationRef = db.collection("reservations");

<<<<<<< HEAD
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
=======
        reservationRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reservationList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    String location = document.getString("location");
                    String website = document.getString("website");
                    String time = document.getString("time");
                    reservationList.add(new Reservation(location, website, time));
                }
                reservationSubject.notifyObservers(reservationList);
            } else {
                Log.w("Firestore", "Error getting documents.", task.getException());
>>>>>>> origin/main
            }
        });
    }

    /**
     * Adds a reservation to Firestore and updates the local list of reservations.
<<<<<<< HEAD
     * @param reservation The reservation to be added.
=======
>>>>>>> origin/main
     */
    private void addReservationToFirestore(Reservation reservation) {
        db.collection("reservations").add(reservation)
                .addOnSuccessListener(documentReference -> {
                    reservationList.add(reservation);
<<<<<<< HEAD
                    updateDiningDisplay();
=======
                    reservationSubject.notifyObservers(reservationList);
>>>>>>> origin/main
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
    }

    /**
<<<<<<< HEAD
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
=======
     * Sorts reservations by date and time, then updates the display.
     */
    private void sortReservationsByDateTime() {
        Collections.sort(reservationList, Comparator.comparing(Reservation::getTime));
        reservationSubject.notifyObservers(reservationList);
    }

    /**
     * Updates the display of reservations with color-coded text for upcoming and expired reservations.
     */
    private void updateDiningDisplay() {
        StringBuilder displayText = new StringBuilder("Reservations:<br>");
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

    @Override
    public void onReservationUpdated(List<Reservation> reservations) {
        updateDiningDisplay();
>>>>>>> origin/main
    }
}
