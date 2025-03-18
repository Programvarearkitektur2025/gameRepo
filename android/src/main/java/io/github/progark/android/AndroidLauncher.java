package io.github.progark.android;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import io.github.progark.Main;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    private DatabaseReference database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        AndroidDatabaseManager dbManager = AndroidDatabaseManager.getInstance();
        auth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        initialize(new Main(dbManager), configuration);

        checkUserLogin();
    }

    private void checkUserLogin() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Log.d("FirebaseAuth", "User is logged in: " + user.getEmail());
        } else {
            Log.d("FirebaseAuth", "No user is logged in");
        }
    }
}
