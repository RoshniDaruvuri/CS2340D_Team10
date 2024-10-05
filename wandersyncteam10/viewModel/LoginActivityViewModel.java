package com.example.wandersyncteam10.viewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivityViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<String> loginError = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            loginError.setValue("Enter Email and Password.");
            return;
        } else if (TextUtils.isEmpty(email)) {
            loginError.setValue("Enter Email.");
            return;
        } else if (TextUtils.isEmpty(password)) {
            loginError.setValue("Enter Password.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.setValue(mAuth.getCurrentUser());
                    } else {
                        String errorMessage = "Login failed: " + task.getException().getMessage();
                        loginError.setValue(errorMessage);
                    }
                });
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }
}

