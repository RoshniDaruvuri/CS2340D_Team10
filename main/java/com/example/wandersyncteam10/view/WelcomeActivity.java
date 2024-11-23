package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wandersyncteam10.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Linking to the layout

        Button startButton = findViewById(R.id.startButton);
        Button quitButton = findViewById(R.id.quitButton);

        // Start button click listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity when "Start" is clicked
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Quit button click listener
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exit the app when "Quit" is clicked
                finish();
            }
        });
    }
}