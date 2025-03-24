package io.github.progark;

import com.badlogic.gdx.Game;


import io.github.progark.Client.Views.Login.LoginView;
import io.github.progark.Client.Views.Menu.CreateGameView;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DatabaseManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private DatabaseManager dbInstance;
    private AuthService authService;


    public Main(DatabaseManager dbManager, AuthService authManager){
        if (dbManager == null){
            return;
        }
        dbInstance = dbManager;
        authService = authManager;

        if (authManager.isUserLoggedIn()) {
            System.out.println("Logged in as: " + authManager.getCurrentUserEmail());
        } else {
            System.out.println("No user logged in.");
        }
    }


    @Override
    public void create() {
        this.setScreen(new LoginView(this, authService));
    }

    public DatabaseManager getDbInstance() {
        return dbInstance;
    }

}

