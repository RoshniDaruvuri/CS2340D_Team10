package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wandersyncteam10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account); // Make sure this matches the XML layout
        mAuth = FirebaseAuth.getInstance();
        EditText usernameEditText = findViewById(R.id.username_create);
        EditText passwordEditText = findViewById(R.id.password_create);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        // Add click listeners or any other logic here
        // Login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to MainActivity when "Start" is clicked
                Intent intent = new Intent(CreateAccountActivity.this, Navigation.class);
                startActivity(intent);
                finish();
            }
        });

        // Register button click listener
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username_create, password_create;
                username_create = String.valueOf(usernameEditText.getText());
                password_create = String.valueOf(passwordEditText.getText());

                mAuth.createUserWithEmailAndPassword(username_create, password_create)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful() && username_create.contains(" ")) {
                                    Toast.makeText(CreateAccountActivity.this, "Authentication created.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(CreateAccountActivity.this, "Authentication failed or username has white space",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

