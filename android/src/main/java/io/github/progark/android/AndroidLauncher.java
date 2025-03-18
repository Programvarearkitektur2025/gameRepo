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
import io.github.progark.android.FirebaseAuthManager;
import io.github.progark.Server.Service.AuthService;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    private DatabaseReference database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        AndroidDatabaseManager dbManager = AndroidDatabaseManager.getInstance();
        AuthService authManager = FirebaseAuthManager.getInstance();

        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true;
        initialize(new Main(dbManager, authManager), configuration);
    }

}
