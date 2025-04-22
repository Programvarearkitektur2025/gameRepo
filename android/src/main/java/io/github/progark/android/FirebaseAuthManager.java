package io.github.progark.android;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.Service.AuthService; // Change to proper interface
import io.github.progark.Server.database.DataCallback;

/*
Class for using for firebase authentication
 */
public class FirebaseAuthManager implements AuthService {

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
/*
 Function for registering a new user
 */
    @Override
    public void signUp(String email, String password, String username, DataCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();
                        UserModel userModel = new UserModel(uid, email, username);

                        FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(uid)
                            .set(userModel)
                            .addOnSuccessListener(aVoid -> {
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


/*
Function for signing in
 */
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
/*
Function for getting the current user
 */
    @Override
    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }
/*
Function for getting the current user email
    */
    @Override
    public String getCurrentUserEmail() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getEmail() : "No user logged in";
    }
/*
Function for getting the current user username
    */

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
/*
 * Function for getting the username from userId
 * @param userId The userId to get the username for
 * @param callback The callback to handle the result
 * @return The username of the user with the given userId
 * @throws Exception If the userId is null or if the user document does not exist
 */
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
    /*
     * Function for getting the logged in username
     */
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
