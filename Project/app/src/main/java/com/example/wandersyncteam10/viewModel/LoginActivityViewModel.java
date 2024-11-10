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

    /**
     * @param application
     * The application context used to initialize the ViewModel.
     * This should not be null.
      */
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * @param email email
     * @param password password
     * If anything is empty, notifies user to enter credentials
     */
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

    /**
     * user
     * @return user
     */
    public LiveData<FirebaseUser> getUser() {

        return user;
    }

    /**
     * loginError
     * @return error
     */
    public LiveData<String> getLoginError() {

        return loginError;
    }
}
