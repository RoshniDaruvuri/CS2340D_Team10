package com.example.wandersyncteam10.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

public class Logistics_Activity extends AppCompatActivity {

    private TextView usernameDisplay;
    private List<String> contributorsList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Find the TextView for displaying usernames
        usernameDisplay = findViewById(R.id.username_display);

        // Initialize the list of contributors
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

        // Other buttons remain unchanged...
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics_Activity.this, Logistics_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics_Activity.this, Destination_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.notesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics_Activity.this, notesActivity.class);
                startActivity(intent);
            }
        });
    }

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

    private void updateContributorsDisplay() {
        StringBuilder contributorsText = new StringBuilder("Contributors:\n");
        for (String contributor : contributorsList) {
            contributorsText.append(contributor).append("\n");
        }
        usernameDisplay.setText(contributorsText.toString());
    }

    // Contributor model class
    public class Contributor {
        private String name;

        public Contributor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}