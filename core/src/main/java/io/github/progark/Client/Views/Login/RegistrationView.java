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

import io.github.progark.Client.Views.View;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;

public class RegistrationView extends View {
    private final Main game;
    private final AuthService authService;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Image background;
    private final Image logo;

    public RegistrationView(Main game, AuthService authService) {
        super();
        this.game = game;
        this.authService = authService;
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

        // Title
        Label titleLabel = new Label("Create Account", skin);
        titleLabel.setFontScale(2.5f);
        table.add(titleLabel).colspan(2).expandX().center().padBottom(30).row();

        // Username field
        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        usernameField.setSize(400, 60);
        table.add(usernameField).colspan(2).width(400).height(60).padBottom(20).row();

        // Email field
        TextField emailField = new TextField("", skin);
        emailField.setMessageText("Email");
        emailField.setSize(400, 60);
        table.add(emailField).colspan(2).width(400).height(60).padBottom(20).row();

        // Password field
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setSize(400, 60);
        table.add(passwordField).colspan(2).width(400).height(60).padBottom(30).row();

        // Register button
        TextButton registerButton = new TextButton("Register", skin);
        registerButton.getLabel().setFontScale(2f);
        registerButton.setSize(400, 80);
        table.add(registerButton).colspan(2).width(400).height(80).row();

        // Back button
        TextButton backButton = new TextButton("Back to Login", skin);
        backButton.getLabel().setFontScale(2f);
        backButton.setSize(400, 80);
        table.add(backButton).colspan(2).width(400).height(80).padTop(20).row();

        // Add button listeners
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String email = emailField.getText();
                String password = passwordField.getText();

                if (!email.isEmpty() && !password.isEmpty()) {
                    String username = usernameField.getText();
                    if (!username.isEmpty()) {
                        authService.signUp(email, password, username, new DataCallback() {
                            @Override
                            public void onSuccess(Object message) {
                                System.out.println("Registration successful!");
                                game.getViewManager().setView(() -> new LoginView(game, authService));
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.out.println("Registration failed: " + e.getMessage());
                            }
                        });
                    } else {
                        System.out.println("Please enter a username");
                    }
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getViewManager().setView(() -> new LoginView(game, authService));
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
