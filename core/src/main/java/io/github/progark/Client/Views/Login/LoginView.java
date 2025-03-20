package io.github.progark.Client.Views.Login;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.progark.Main;

public class LoginView implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private Texture logoTexture, backgroundTexture;
    private Image logo, background;

    public LoginView(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load UI Skin
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load Background and Logo Textures
        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        logoTexture = new Texture(Gdx.files.internal("ThinkFastLogo.png")); // Removed space in filename

        background = new Image(backgroundTexture);
        logo = new Image(logoTexture);

        background.setFillParent(true);

        // Add Background First
        stage.addActor(background);

        // Create Table Layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Welcome Label
        Label titleLabel = new Label("Welcome back!", skin);
        titleLabel.setFontScale(2f);

        // Username Field
        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        // Password Field
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        // Login Button
        TextButton loginButton = new TextButton("Log in", skin); // Uses same style as HomeView
        loginButton.getLabel().setFontScale(1.5f);

        // Register Label
        Label registerLabel = new Label("Don't have an account? Register here", skin);
        registerLabel.setFontScale(0.9f);

        // Add UI Elements to Table
        table.add(logo).size(200, 200).padBottom(20).row();
        table.add(titleLabel).padBottom(30).row();
        table.add(usernameField).width(350).height(50).padBottom(20).row();
        table.add(passwordField).width(350).height(50).padBottom(30).row();
        table.add(loginButton).width(350).height(60).padBottom(20).row();
        table.add(registerLabel).padBottom(20).row();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        logoTexture.dispose();
    }
}
