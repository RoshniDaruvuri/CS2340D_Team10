package com.example.wandersyncteam10.viewModel;

import android.util.Log;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Handles the backend operations for creating a user account.
 */
public class CreateAccountActivityBackEnd {
    private FirebaseAuth mAuth;

    /**
     * Initializes the CreateAccountActivityBackEnd instance.
     * This constructor sets up the Firebase Authentication instance.
     */
    public CreateAccountActivityBackEnd() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Creates a new user account with the specified email and password.
     *
     * @param email    The email address for the new account.
     * @param password The password for the new account.
     * @param context  The context used to display Toast messages.
     */
    public void createAccount(String email, String password, Context context) {
        if (email.isEmpty() || password.isEmpty() || email.contains(" ") || password.contains(" ")) {
            Toast.makeText(context, "Username or password cannot be empty or contain white space, try again.",
                    Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

