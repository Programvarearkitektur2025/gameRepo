package io.github.progark;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Client.Views.Login.LoginView;
import io.github.progark.Client.Views.ViewManager;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DatabaseManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private AuthService authService;
    private ViewManager viewManager;

    public Main(DatabaseManager dbManager, AuthService authManager) {
        if (dbManager == null || authManager == null) {
            throw new IllegalArgumentException("DatabaseManager and AuthService cannot be null");
        }
        authService = authManager;

        if (authManager.isUserLoggedIn()) {
            System.out.println("Logged in as: " + authManager.getCurrentUserEmail());
        } else {
            System.out.println("No user logged in.");
        }
    }

    @Override
    public void create() {
        viewManager = new ViewManager();
        viewManager.setView(new LoginView(this, authService));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        viewManager.update(delta);
        viewManager.render();
    }

    @Override
    public void dispose() {
        if (viewManager != null) {
            viewManager.dispose();
        }
        super.dispose();
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

    public AuthService getAuthService() {
        return authService;
    }
}
