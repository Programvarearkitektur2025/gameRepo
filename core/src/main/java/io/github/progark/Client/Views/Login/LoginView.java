package io.github.progark.Client.Views.Login;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.progark.Client.Views.Menu.CreateGameView;
import io.github.progark.Client.Views.Menu.LandingView;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;


public class LoginView implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private Texture logoTexture, backgroundTexture;
    private Image logo, background;

    public LoginView(Main game, AuthService authService) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load UI Skin
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load Background and Logo Textures
        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        logoTexture = new Texture(Gdx.files.internal("ThinkFastLogo.png"));

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
        Label statusLabel = new Label("", skin);
        statusLabel.setFontScale(2f);

        // Login button onclick logic
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String email = usernameField.getText();
                String password = passwordField.getText();

                if (!email.isEmpty() && !password.isEmpty()) {
                    authService.signIn(email, password, new DataCallback() {
                        @Override
                        public void onSuccess(Object message) {
                            System.out.println("Sign-in successful! User: " + (String) message);
                            View.safeSetScreen(game, () -> new CreateGameView(game));

                        }

                        @Override
                        public void onFailure(Exception e) {
                            System.out.println("Sign-in failed: " + e.getMessage());
                        }
                    });
                } else {
                    statusLabel.setText("Please enter valid credentials.");
                }
            }
        });
        // Button that takes you to RegistrationView
        TextButton registerButton = new TextButton("New? Register user here", skin);
        registerButton.getLabel().setFontScale(4f); // Makes text bigger
        registerButton.setSize(400, 200);
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new RegistrationView(game,authService));
            }
        });
        table.add(registerButton).width(400).height(200).pad(20); // Makes button larger and adds spacing
        table.row();

        // Add UI Elements to Table
        table.add(logo).size(200, 200).padBottom(20).row();
        table.add(titleLabel).padBottom(30).row();
        table.add(usernameField).width(350).height(50).padBottom(20).row();
        table.add(passwordField).width(350).height(50).padBottom(30).row();
        table.add(loginButton).width(350).height(60).padBottom(20).row();
        table.add(registerButton).padBottom(20).row();
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
