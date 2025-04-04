package io.github.progark.android;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.Service.AuthService; // Change to proper interface
import io.github.progark.Server.database.DataCallback;


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
    public void signUp(String email, String password, String username, DataCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();
                        UserModel userModel = new UserModel(uid, email, username);

                        // Opprett bruker i "Users"
                        FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(uid)
                            .set(userModel)
                            .addOnSuccessListener(aVoid -> {
                                // Legg brukernavn med score=0 til i "leaderboard" -> "Leaderboard - 2025"
                                FirebaseFirestore.getInstance()
                                    .collection("leaderboard")
                                    .document("Leaderboard - 2025")
                                    .update(username, 0)
                                    .addOnSuccessListener(unused -> {
                                        if (callback != null) callback.onSuccess(userModel);
                                    })
                                    .addOnFailureListener(e -> {
                                        if (callback != null) callback.onFailure(e);
                                    });
                            })
                            .addOnFailureListener(e -> {
                                if (callback != null) callback.onFailure(e);
                            });
                    } else {
                        if (callback != null) {
                            callback.onFailure(new Exception("FirebaseUser is null after registration"));
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(task.getException());
                    }
                }
            })
            .addOnFailureListener(e -> {
                if (callback != null) {
                    callback.onFailure(e);
                }
            });
    }



    @Override
    public void signIn(String email, String password, DataCallback dataCallback) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    dataCallback.onSuccess("We did it");
                } else {
                    dataCallback.onFailure(new Exception("Sign-in failed", task.getException()));
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

    @Override
    public String getCurrentUser(DataCallback callback) {
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
            callback.onFailure(new Exception("No user is currently logged in"));
            return null;
        }

        String uid = firebaseUser.getUid();

        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(uid)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String email = documentSnapshot.getString("email");
                    String username = documentSnapshot.getString("username");

                    UserModel userModel = new UserModel(uid, email, username);
                    callback.onSuccess(userModel);
                } else {
                    callback.onFailure(new Exception("User document does not exist"));
                }
            })
            .addOnFailureListener(callback::onFailure);
        return uid;
    }

    @Override
    public void getUsernameFromUserId(String userId, DataCallback callback) {
        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(userId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String email = documentSnapshot.getString("email");
                    String username = documentSnapshot.getString("username");

                    if (username != null) {
                        // Returner brukernavn som suksess
                        callback.onSuccess(username);
                    } else {
                        callback.onFailure(new Exception("User doc found, but 'username' field is null"));
                    }
                } else {
                    callback.onFailure(new Exception("No user document for userId=" + userId));
                }
            })
            .addOnFailureListener(callback::onFailure);
    }
    @Override
    public void getLoggedInUsername(DataCallback callback) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            callback.onFailure(new Exception("No user is currently logged in"));
            return;
        }

        String uid = firebaseUser.getUid();

        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(uid)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    if (username != null) {
                        callback.onSuccess(username);
                    } else {
                        callback.onFailure(new Exception("Username is null in user doc"));
                    }
                } else {
                    callback.onFailure(new Exception("User document does not exist for " + uid));
                }
            })
            .addOnFailureListener(callback::onFailure);
    }


}
