package com.example.wandersyncteam10.view;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseSubject<T> {

    private final DatabaseReference databaseReference;
    private final Class<T> modelClass;
    private DatabaseObserver<T> observer;

    public FirebaseDatabaseSubject(DatabaseReference databaseReference, Class<T> modelClass) {
        this.databaseReference = databaseReference;
        this.modelClass = modelClass;
    }

    public void setObserver(DatabaseObserver<T> observer) {
        this.observer = observer;
    }

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
