package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.progark.Client.Views.View;
import io.github.progark.Main;

public class LandingView extends View {
    private final Main game;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Image background;
    private final Image logo;

    public LandingView(Main game) {
        super();
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load textures
        this.backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        this.logoTexture = new Texture(Gdx.files.internal("ThinkFastLogo.png"));

        this.background = new Image(backgroundTexture);
        this.logo = new Image(logoTexture);
    }

    @Override
    protected void initialize() {
        // Set up background
        background.setFillParent(true);
        stage.addActor(background);

        // Create main table
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add logo
        table.add(logo).size(200, 200).padBottom(50).row();

        // Welcome message
        Label welcomeLabel = new Label("Welcome to ThinkFast!", skin);
        welcomeLabel.setFontScale(2.5f);
        table.add(welcomeLabel).padBottom(50).row();
        /**
        // Create game button
        TextButton createGameButton = new TextButton("Create Game", skin);
        createGameButton.getLabel().setFontScale(2f);
        createGameButton.setSize(400, 80);
        table.add(createGameButton).width(400).height(80).padBottom(20).row();

        //

        // Join game button
        TextButton joinGameButton = new TextButton("Join Game", skin);
        joinGameButton.getLabel().setFontScale(2f);
        joinGameButton.setSize(400, 80);
        table.add(joinGameButton).width(400).height(80).padBottom(20).row();

        // Settings button
        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.getLabel().setFontScale(2f);
        settingsButton.setSize(400, 80);
        table.add(settingsButton).width(400).height(80).row();

        // Add button listeners
        createGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getViewManager().setView(() -> new CreateGameView(game));
            }
        });

        joinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Implement join game functionality
                System.out.println("Join game clicked");
                game.getViewManager().setView(() -> new JoinGameView(game));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Implement settings functionality
                System.out.println("Settings clicked");
            }
        });**/
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        logoTexture.dispose();
    }
}
