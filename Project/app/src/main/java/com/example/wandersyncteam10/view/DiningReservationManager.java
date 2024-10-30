package com.example.wandersyncteam10.view;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error adding document", e);
                    }
                });
    }
}

