package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wandersyncteam10.R;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account); // Make sure this matches the XML layout

        EditText usernameEditText = findViewById(R.id.username_create);
        EditText passwordEditText = findViewById(R.id.password_create);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        // Add click listeners or any other logic here
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity when "Start" is clicked
                Intent intent = new Intent(CreateAccountActivity.this, Navigation.class);
                startActivity(intent);
            }
        });
    }
}
