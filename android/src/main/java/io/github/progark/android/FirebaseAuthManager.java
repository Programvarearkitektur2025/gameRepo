package io.github.progark.android;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.github.progark.Server.Service.AuthService; // Change to proper interface

public class FirebaseAuthManager implements AuthService { // Change interface

    private static FirebaseAuthManager authInstance;
    private FirebaseAuth auth;

    private FirebaseAuthManager() {
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized FirebaseAuthManager getInstance() {
        if (authInstance == null) {
            authInstance = new FirebaseAuthManager();
        }
        return authInstance;
    }

    @Override
    public void signUp(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    Log.d("FirebaseAuth", "User registered: " + (user != null ? user.getEmail() : "Unknown"));
                } else {
                    Log.e("FirebaseAuth", "Registration failed", task.getException());
                }
            });
    }

    @Override
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    Log.d("FirebaseAuth", "User logged in: " + (user != null ? user.getEmail() : "Unknown"));
                } else {
                    Log.e("FirebaseAuth", "Login failed", task.getException());
                }
            });
    }

    @Override
    public void signOut() {
        auth.signOut();
        Log.d("FirebaseAuth", "User signed out.");
    }

    @Override
    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    @Override
    public String getCurrentUserEmail() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getEmail() : "No user logged in";
    }
}
