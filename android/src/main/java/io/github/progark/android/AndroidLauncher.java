package io.github.progark.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import io.github.progark.Main;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        AndroidDatabaseManager dbManager = AndroidDatabaseManager.getInstance();

        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        initialize(new Main(dbManager), configuration);
    }
}
