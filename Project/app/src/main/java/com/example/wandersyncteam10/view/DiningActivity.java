//package com.example.wandersyncteam10.view;
//
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.icu.text.SimpleDateFormat;
//import android.os.Bundle;
//import android.text.Html;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.wandersyncteam10.Model.Reservation;
//import com.example.wandersyncteam10.R;
//import com.example.wandersyncteam10.viewModel.ReservationObserver;
//import com.example.wandersyncteam10.viewModel.ReservationSubject;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//
///**
// * DiningActivity handles the display and management of dining reservations.
// */
//public class DiningActivity extends AppCompatActivity implements ReservationObserver {
//
//    private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm";
//    private TextView diningDisplay;
//    private List<Reservation> reservationList;
//    private FirebaseFirestore db;
//    private ReservationSubject reservationSubject;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dining);
//
//        db = FirebaseFirestore.getInstance();
//        reservationSubject = new ReservationSubject();
//        reservationSubject.addObserver(this);
//
//        diningDisplay = findViewById(R.id.dining_display);
//        reservationList = new ArrayList<>();
//
//        loadReservationsFromFirestore();
//
//        findViewById(R.id.buttonAddReservation).setOnClickListener(view -> showAddReservationDialog());
//        findViewById(R.id.buttonSortReservations).setOnClickListener(view -> sortReservationsByDateTime());
//
//        // Example dashboard navigation buttons
//        findViewById(R.id.button).setOnClickListener(view ->
//                startActivity(new Intent(this, LogisticsActivity.class))
//        );
//
//        findViewById(R.id.button).setOnClickListener(view -> {
//            Intent intent = new Intent(DiningActivity.this, LogisticsActivity.class);
//            startActivity(intent);
//        });
//
//        findViewById(R.id.button2).setOnClickListener(view -> {
//            Intent intent = new Intent(DiningActivity.this, DestinationActivity.class);
//            startActivity(intent);
//        });
//
//        findViewById(R.id.button3).setOnClickListener(view -> {
//            Intent intent = new Intent(DiningActivity.this, DiningActivity.class);
//            startActivity(intent);
//        });
//
//        findViewById(R.id.button4).setOnClickListener(view -> {
//            Intent intent = new Intent(DiningActivity.this, AccommodationsActivity.class);
//            startActivity(intent);
//        });
//
//        findViewById(R.id.button5).setOnClickListener(view -> {
//            Intent intent = new Intent(DiningActivity.this, CommunityActivity.class);
//            startActivity(intent);
//        });
//
////        findViewById(R.id.button6).setOnClickListener(view -> {
////            Intent intent = new Intent(DiningActivity.this, TravelActivity.class);
////            startActivity(intent);
////        });
//    }
//
//    /**
//     * Displays a dialog to add a reservation.
//     */
//    private void showAddReservationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add Reservation");
//
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_add_dining, null);
//        builder.setView(dialogView);
//
//        final EditText locationInput = dialogView.findViewById(R.id.editTextLocation);
//        final EditText websiteInput = dialogView.findViewById(R.id.editTextWebsite);
//        final EditText timeInput = dialogView.findViewById(R.id.editTextTime);
//
//        builder.setPositiveButton("Add", (dialog, which) -> {
//            String location = locationInput.getText().toString();
//            String website = websiteInput.getText().toString();
//            String time = timeInput.getText().toString();
//            if (!location.isEmpty() && !website.isEmpty()
//                    && isValidTimeFormat(time) && !isDuplicateReservation(location, time)) {
//                addReservationToFirestore(new Reservation(location, website, time));
//            } else {
//                String message = (!isValidTimeFormat(time)) ? "Invalid time format. Use "
//                        + DATE_FORMAT : "Duplicate reservation.";
//                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//        builder.show();
//    }
//
//    /**
//     * Validates that the time string is in the correct format.
//     *
//     * @param time The time string to validate.
//     * @return True if the time is valid; false otherwise.
//     */
//    private boolean isValidTimeFormat(String time) {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
//            dateFormat.setLenient(false);
//            dateFormat.parse(time);
//            return true;
//        } catch (ParseException e) {
//            return false;
//        }
//    }
//
//    /**
//     * Checks if a reservation with the same location and time already exists.
//     *
//     * @param location The reservation location.
//     * @param time     The reservation time.
//     * @return True if a duplicate exists; false otherwise.
//     */
//    private boolean isDuplicateReservation(String location, String time) {
//        for (Reservation reservation : reservationList) {
//            if (reservation.getLocation().equalsIgnoreCase(location) && reservation.getTime().equals(time)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Loads the list of reservations from Firestore and updates the display.
//     */
//    private void loadReservationsFromFirestore() {
//        CollectionReference reservationRef = db.collection("reservations");
//
//        reservationRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                reservationList.clear();
//                for (DocumentSnapshot document : task.getResult()) {
//                    String location = document.getString("location");
//                    String website = document.getString("website");
//                    String time = document.getString("time");
//                    reservationList.add(new Reservation(location, website, time));
//                }
//                updateDiningDisplay();
//                reservationSubject.notifyObservers(reservationList);
//            } else {
//                Log.w("Firestore", "Error getting documents.", task.getException());
//            }
//        });
//    }
//
//    /**
//     * Adds a reservation to Firestore and updates the local list of reservations.
//     *
//     * @param reservation The reservation to add.
//     */
//    private void addReservationToFirestore(Reservation reservation) {
//        db.collection("reservations").add(reservation)
//                .addOnSuccessListener(documentReference -> {
//                    reservationList.add(reservation);
//                    updateDiningDisplay();
//                    reservationSubject.notifyObservers(reservationList);
//                })
//                .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
//    }
//
//    /**
//     * Updates the display of reservations in the TextView.
//     */
//    private void updateDiningDisplay() {
//        StringBuilder displayText = new StringBuilder("Reservations:\n");
//        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
//        Date currentDate = new Date();
//
//        for (Reservation reservation : reservationList) {
//            try {
//                Date reservationDate = dateFormat.parse(reservation.getTime());
//                String color = reservationDate != null && reservationDate.before(currentDate)
//                        ? "#FF0000" : "#00FF00";
//                displayText.append("<font color='").append(color).append("'>")
//                        .append(reservation.getLocation()).append(" - ")
//                        .append(reservation.getWebsite()).append(" at ")
//                        .append(reservation.getTime()).append("</font><br>");
//            } catch (ParseException e) {
//                Log.w("DiningActivity", "Error parsing date format", e);
//            }
//        }
//
//        diningDisplay.setText(Html.fromHtml(displayText.toString()));
//    }
//
//    /**
//     * Sorts reservations by their time.
//     */
//    private void sortReservationsByDateTime() {
//        Collections.sort(reservationList, Comparator.comparing(Reservation::getTime));
//        reservationSubject.notifyObservers(reservationList);
//    }
//
//    @Override
//    public void onReservationUpdated(List<Reservation> reservations) {
//        updateDiningDisplay();
//    }
//}




package com.example.wandersyncteam10.view;

import com.example.wandersyncteam10.viewModel.ReservationObserver;
import com.example.wandersyncteam10.viewModel.ReservationSubject;
import com.example.wandersyncteam10.Model.Reservation;


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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DiningActivity handles the display and management of dining reservations.
 */
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

        diningDisplay = findViewById(R.id.dining_display);
        reservationList = new ArrayList<>();

        // Listen for real-time updates
        listenToReservations();

        findViewById(R.id.buttonAddReservation).setOnClickListener(view -> showAddReservationDialog());
        findViewById(R.id.buttonSortReservations).setOnClickListener(view -> sortReservationsByDateTime());

        // Example dashboard navigation buttons
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
    }

    /**
     * Adds a Firestore listener for real-time updates.
     */
    private void listenToReservations() {
        CollectionReference reservationRef = db.collection("reservations");
        reservationRef.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Log.w("Firestore", "Listen failed.", error);
                return;
            }

            if (snapshots != null) {
                reservationList.clear();
                for (DocumentSnapshot document : snapshots) {
                    String location = document.getString("location");
                    String website = document.getString("website");
                    String time = document.getString("time");
                    reservationList.add(new Reservation(location, website, time));
                }
                updateDiningDisplay();
                reservationSubject.notifyObservers(reservationList);
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

        builder.setPositiveButton("Add", (dialog, which) -> {
            String location = locationInput.getText().toString();
            String website = websiteInput.getText().toString();
            String time = timeInput.getText().toString();

            if (!location.isEmpty() && !website.isEmpty()
                    && isValidTimeFormat(time) && !isDuplicateReservation(location, time)) {
                addReservationToFirestore(new Reservation(location, website, time));
            } else {
                String message = (!isValidTimeFormat(time)) ? "Invalid time format. Use " + DATE_FORMAT : "Duplicate reservation.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Updates the display of reservations in the TextView with color coding.
     * Expired reservations are displayed in red, and upcoming reservations are in green.
     */
    private void updateDiningDisplay() {
        StringBuilder displayText = new StringBuilder("<h3>Reservations:</h3>"); // HTML formatting for better styling
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setLenient(false);
        Date currentDate = new Date(); // Current time for comparison

        for (Reservation reservation : reservationList) {
            try {
                Date reservationDate = dateFormat.parse(reservation.getTime());
                String color;

                if (reservationDate != null && reservationDate.before(currentDate)) {
                    color = "#FF0000"; // Red for expired reservations
                } else {
                    color = "#00FF00"; // Green for upcoming reservations
                }

                displayText.append("<font color='").append(color).append("'>")
                        .append(reservation.getLocation()).append(" - ")
                        .append("<a href='").append(reservation.getWebsite()).append("'>")
                        .append(reservation.getWebsite()).append("</a> at ")
                        .append(reservation.getTime())
                        .append("</font><br>");
            } catch (ParseException e) {
                Log.w("DiningActivity", "Error parsing reservation date", e);
                displayText.append("<font color='#000000'>")
                        .append(reservation.getLocation()).append(" - ")
                        .append(reservation.getWebsite()).append(" at ")
                        .append(reservation.getTime()).append(" (Invalid date format)</font><br>");
            }
        }

        diningDisplay.setText(Html.fromHtml(displayText.toString(), Html.FROM_HTML_MODE_COMPACT));
    }


    /**
     * Validates that the time string is in the correct format.
     *
     * @param time The time string to validate.
     * @return True if the time is valid; false otherwise.
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
     *
     * @param location The reservation location.
     * @param time     The reservation time.
     * @return True if a duplicate exists; false otherwise.
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
     * Adds a reservation to Firestore.
     *
     * @param reservation The reservation to add.
     */
    private void addReservationToFirestore(Reservation reservation) {
        db.collection("reservations").add(reservation)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Reservation added!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
    }

    /**
     * Sorts reservations by their time.
     */
    private void sortReservationsByDateTime() {
        reservationList.sort((r1, r2) -> r1.getTime().compareTo(r2.getTime()));
        updateDiningDisplay();
    }

    @Override
    public void onReservationUpdated(List<Reservation> reservations) {
        updateDiningDisplay();
    }
}

