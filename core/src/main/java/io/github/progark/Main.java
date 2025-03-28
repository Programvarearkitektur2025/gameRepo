package io.github.progark;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;


import io.github.progark.Client.Views.Login.LoginView;
import io.github.progark.Client.Views.Login.RegistrationView;
import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Client.Views.Menu.LandingView;
import io.github.progark.Client.Views.View;
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


       this.setScreen(new HomeView(this, authService));
        //View.safeSetScreen(this, () -> new RegistrationView(this, authService));

    }

    public DatabaseManager getDbInstance() {
        return dbInstance;
    }

}

