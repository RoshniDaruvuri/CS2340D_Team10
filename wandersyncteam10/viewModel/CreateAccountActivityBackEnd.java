package com.example.wandersyncteam10.viewModel;

import android.util.Log;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivityBackEnd {
    public FirebaseAuth mAuth;

    // Constructor
    public CreateAccountActivityBackEnd() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Method to handle account creation
    public void createAccount(String email, String password, Context context) {
        // Basic validation to check for white space
        if (email.isEmpty() || password.isEmpty() || email.contains(" ") || password.contains(" ")) {
            Toast.makeText(context, "Username or password cannot be empty or contain white space, try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("REGISTERING", "TRYING TO REGISTER");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("REGISTERING", "Registration successful!");
                            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("REGISTERING", "Registration failed: " + task.getException().getMessage());
                            Toast.makeText(context, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
