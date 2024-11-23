package com.example.wandersyncteam10.view;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic subject for observing data changes in Firebase Realtime Database.
 *
 * @param <T> The type of data being observed.
 */
public class FirebaseDatabaseSubject<T> {

    private final DatabaseReference databaseReference;
    private final Class<T> modelClass;
    private DatabaseObserver<T> observer;

    /**
     * Constructs a FirebaseDatabaseSubject.
     *
     * @param databaseReference The database reference to observe.
     * @param modelClass        The class type of the data being observed.
     */
    public FirebaseDatabaseSubject(DatabaseReference databaseReference, Class<T> modelClass) {
        this.databaseReference = databaseReference;
        this.modelClass = modelClass;
    }

    /**
     * Sets the observer for this FirebaseDatabaseSubject.
     *
     * @param observer The observer to be notified of data changes.
     */
    public void setObserver(DatabaseObserver<T> observer) {
        this.observer = observer;
    }

    /**
     * Starts listening for data changes on the specified database reference.
     */
    public void startListening() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (observer != null) {
                    List<T> dataList = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        T item = data.getValue(modelClass);
                        if (item != null) {
                            dataList.add(item);
                        }
                    }
                    observer.onDataUpdated(dataList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (observer != null) {
                    observer.onError(error.getMessage());
                }
            }
        });
    }
}
