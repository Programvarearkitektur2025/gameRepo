package io.github.progark;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Server.database.databaseManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private databaseManager dbInstance;

    public Main(databaseManager dbManager) {
        if (dbManager != null) {
            dbInstance = dbManager;
        }
    }

    @Override
    public void create() {
        this.setScreen(new HomeView(this)); // Start with the UI screen
    }
}
