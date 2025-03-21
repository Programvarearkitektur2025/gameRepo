package io.github.progark;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;


import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Client.Views.Login.LoginView;
import io.github.progark.Client.Views.Login.RegistrationView;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.FirebaseAuthManager;
import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Server.database.databaseManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private databaseManager dbInstance;
    private AuthService authService;


    public Main(databaseManager dbManager, AuthService authManager){
        if (dbManager == null || authManager == null){
            System.out.println("Warning: Missing databaseManager or AuthService");
            return;
        }
        dbInstance = dbManager;
        authService = authManager;
    }


    @Override
    public void create() {
        this.setScreen(new GameView());
        //this.setScreen(new HomeView(this));
    }
}


