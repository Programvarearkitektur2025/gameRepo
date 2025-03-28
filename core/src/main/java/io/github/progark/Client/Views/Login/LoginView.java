package io.github.progark.Client.Views.Login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Client.Views.Menu.LandingView;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;

public class LoginView extends View {
    private final Main game;
    private final AuthService authService;
    private final Skin skin;
    private final Texture logoTexture, backgroundTexture;
    private final Image logo, background;
    private Label statusLabel;

    public LoginView(Main game, AuthService authService) {
        super();
        this.game = game;
        this.authService = authService;

        // Load resources
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        logoTexture = new Texture(Gdx.files.internal("ThinkFastLogo.png"));

        background = new Image(backgroundTexture);
        logo = new Image(logoTexture);
    }

    @Override
    protected void initialize() {
        // If user is already logged in we dont bother setting up UI
        /*
        if (authService.isUserLoggedIn()){
            game.getViewManager().setView(() -> new HomeView(game, authService));
        }

         */

        // Set up UI
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Welcome Label
        Label titleLabel = new Label("Welcome back!", skin);
        titleLabel.setFontScale(4f);

        // Username Field
        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Email");
        usernameField.getStyle().font.getData().setScale(2f);

        // Password Field
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.getStyle().font.getData().setScale(2f);

        // Login Button
        TextButton loginButton = new TextButton("Log in", skin);
        loginButton.getLabel().setFontScale(3f);
        statusLabel = new Label("", skin);
        statusLabel.setFontScale(4f);

        setupLoginButton(loginButton, usernameField, passwordField);

        // Register Button
        TextButton registerButton = new TextButton("New? Register user here", skin);
        registerButton.getLabel().setFontScale(2.5f);
        registerButton.setSize(400, 200);

        TextButton.TextButtonStyle clearStyle = new TextButton.TextButtonStyle(registerButton.getStyle());
        clearStyle.up = null;
        clearStyle.down = null;
        clearStyle.over = null;
        registerButton.setStyle(clearStyle);

        setupRegisterButton(registerButton);



        // Add UI Elements to Table
        table.add(logo).size(600, 600).padBottom(20).row();
        table.add(titleLabel).padBottom(30).row();
        table.add(usernameField).width(500).height(100).padBottom(20).row();
        table.add(passwordField).width(500).height(100).padBottom(30).row();
        table.add(loginButton).width(500).height(120).padBottom(50).row();
        table.add(registerButton).width(500).height(120).padBottom(20).row();
    }

    private void setupLoginButton(TextButton loginButton, TextField usernameField, TextField passwordField) {
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
                            game.getViewManager().setView(() -> new HomeView(game, authService));
                        }

                        @Override
                        public void onFailure(Exception e) {
                            System.out.println("Sign-in failed: " + e.getMessage());
                            statusLabel.setText("Login failed: " + e.getMessage());
                        }
                    });
                } else {
                    statusLabel.setText("Please enter valid credentials.");
                }
            }
        });
    }

    private void setupRegisterButton(TextButton registerButton) {
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getViewManager().setView(() -> new RegistrationView(game, authService));
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        logoTexture.dispose();
    }
}
