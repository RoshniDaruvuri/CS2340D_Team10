package com.example.wandersyncteam10.view;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiningReservationManager {
    private FirebaseFirestore db;

    public DiningReservationManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void addDiningReservation(String location, String website, String[] reviews, Date reservationTime) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("location", location);
        reservation.put("website", website);
        reservation.put("reviews", Arrays.asList(reviews));
        reservation.put("reservationTime", reservationTime);

        db.collection("diningReservations")
                .add(reservation)
                .addOnSuccessListener(documentReference -> Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Firebase", "Error adding document", e));
    }

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

    public interface ReservationFetchListener {
        void onFetchComplete(ArrayList<Reservation> reservations);
        void onFetchFailed(Exception e);
    }
}