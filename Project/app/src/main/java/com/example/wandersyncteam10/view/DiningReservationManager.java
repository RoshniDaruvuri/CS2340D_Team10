package com.example.wandersyncteam10.view;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiningReservationManager {
    private FirebaseFirestore db;

    /**
     * Constructor for DiningReservationManager.
     * Initializes the Firestore database instance.
     */
    public DiningReservationManager() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Adds a new dining reservation to the Firestore database.
     *
     * @param location        The location of the reservation.
     * @param website         The website associated with the reservation.
     * @param reviews         An array of reviews for the dining place.
     * @param reservationTime The date and time of the reservation.
     */
    public void addDiningReservation(String location, String website, String[] reviews, Date reservationTime) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("location", location);
        reservation.put("website", website);
        reservation.put("reviews", Arrays.asList(reviews));
        reservation.put("reservationTime", reservationTime);

        db.collection("diningReservations")
                .add(reservation)
                .addOnSuccessListener(documentReference -> Log.d("Firebase",
                        "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Firebase", "Error adding document", e));
    }

    /**
     * Fetches dining reservations from the Firestore database.
     *
     * @param listener A listener to handle the result of the fetch operation.
     */
    public void fetchDiningReservations(final ReservationFetchListener listener) {
        db.collection("diningReservations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Reservation> reservations = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String location = document.getString("location");
                            String website = document.getString("website");
                            String time = document.getDate("reservationTime").toString();
                            reservations.add(new Reservation(location, website, time));
                        }
                        listener.onFetchComplete(reservations);
                    } else {
                        listener.onFetchFailed(task.getException());
                    }
                });
    }

    /**
     * A listener interface for handling dining reservation fetch results.
     */
    public interface ReservationFetchListener {

        /**
         * Called when the fetch operation is successful.
         *
         * @param reservations A list of fetched reservations.
         */
        void onFetchComplete(ArrayList<Reservation> reservations);

        /**
         * Called when the fetch operation fails.
         *
         * @param e The exception that caused the failure.
         */
        void onFetchFailed(Exception e);
    }
}
