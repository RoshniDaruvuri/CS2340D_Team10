package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wandersyncteam10.R;
import com.example.wandersyncteam10.view.ReservationAdapter; // Your custom adapter
import com.example.wandersyncteam10.view.Reservation; // Your model class
import com.example.wandersyncteam10.view.DiningReservationManager; // Import your manager class
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DiningActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter; // Adapter for RecyclerView
    private ArrayList<Reservation> reservationList; // List to hold reservations
    private DiningReservationManager reservationManager; // Dining reservation manager instance
    private LinearLayout reservationFormLayout;
    private EditText editTextLocation, editTextWebsite, editTextTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the DiningReservationManager
        reservationManager = new DiningReservationManager();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        reservationList = new ArrayList<>();
        reservationAdapter = new ReservationAdapter(reservationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reservationAdapter);

        // Initialize Views
        reservationFormLayout = findViewById(R.id.reservationFormLayout);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextTime = findViewById(R.id.editTextTime);

        // Show reservation form
        findViewById(R.id.buttonAddReservation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservationFormLayout.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = editTextLocation.getText().toString();
                String website = editTextWebsite.getText().toString();
                String timeString = editTextTime.getText().toString();

                // Debugging logs
                Log.d("DiningActivity", "Location: " + location);
                Log.d("DiningActivity", "Website: " + website);
                Log.d("DiningActivity", "Time: " + timeString);

                // Parse the time string into a Date object
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date reservationTime = null;
                try {
                    reservationTime = dateFormat.parse(timeString);
                } catch (ParseException e) {
                    Log.e("DiningActivity", "Error parsing date", e);
                    Toast.makeText(DiningActivity.this, "Invalid date format!", Toast.LENGTH_SHORT).show();
                }

                if (reservationTime != null) {
                    reservationManager.addDiningReservation(location, website, new String[]{}, reservationTime);

                    Reservation reservation = new Reservation(location, website, timeString);
                    reservationList.add(reservation);
                    reservationAdapter.notifyItemInserted(reservationList.size() - 1);

                    // Clear fields and hide form
                    editTextLocation.setText("");
                    editTextWebsite.setText("");
                    editTextTime.setText("");
                    reservationFormLayout.setVisibility(View.GONE);
                }
            }
        });


        // Navigation buttons
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
            Intent intent = new Intent(DiningActivity.this, TransportationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button6).setOnClickListener(view -> {
            Intent intent = new Intent(DiningActivity.this, TravelActivity.class);
            startActivity(intent);
        });
    }
}
