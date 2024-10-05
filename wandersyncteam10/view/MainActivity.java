package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wandersyncteam10.R;
import com.example.wandersyncteam10.viewModel.LoginActivityViewModel;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;
    private Button createAccountButton;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        // Initialize ViewModel
        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        // Create Account Button Click Listener
        createAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
            finish();
        });

        // Observe user login status
        loginActivityViewModel.getUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(MainActivity.this, "User has logged in!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Navigation.class);
                startActivity(intent);
                finish();
            }
        });

        // Observe login error messages
        loginActivityViewModel.getLoginError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Login Button Click Listener
        loginButton.setOnClickListener(view -> {
            String email = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            loginActivityViewModel.loginUser(email, password);
        });
    }
}

