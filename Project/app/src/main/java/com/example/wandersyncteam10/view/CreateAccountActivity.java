package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wandersyncteam10.R;
import com.example.wandersyncteam10.viewModel.CreateAccountActivityBackEnd;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    private CreateAccountActivityBackEnd viewModel;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText usernameEditText = findViewById(R.id.username_create);
        EditText passwordEditText = findViewById(R.id.password_create);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        viewModel = new CreateAccountActivityBackEnd();

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        registerButton.setOnClickListener(view -> {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(CreateAccountActivity.this, "Please enter both email and password.",
                        Toast.LENGTH_SHORT).show();
                return;
            } else {
                viewModel.createAccount(email, password, CreateAccountActivity.this);
                //switch to Logistics_Activity after registration
                Intent intent = new Intent(CreateAccountActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });
    }
}
