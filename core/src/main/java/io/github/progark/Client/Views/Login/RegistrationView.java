package io.github.progark.Client.Views.Login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;

public class RegistrationView implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private AuthService authService;

    public RegistrationView(Main game, AuthService authService) {
        this.game = game;
        this.authService = authService;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Ensure the file exists

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        // Button that takes you to LoginView
        TextButton myButton = new TextButton("Already registered? Click here", skin);
        myButton.getLabel().setFontScale(4f); // Makes text bigger
        myButton.setSize(400, 200);
        myButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoginView(game,authService));
            }
        });
        table.add(myButton).width(400).height(200).pad(20); // Makes button larger and adds spacing
        table.row();

        // Create UI elements with increased size
        // Email input field
        Label emailLabel = new Label("Email:", skin);
        emailLabel.setFontScale(2f);
        TextField emailField = new TextField("", skin);
        emailField.setStyle(emailField.getStyle());
        emailField.getStyle().font.getData().setScale(2f);

        // Password input field
        Label passwordLabel = new Label("Password:", skin);
        passwordLabel.setFontScale(2f);
        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.getStyle().font.getData().setScale(2f);

        // Register button
        TextButton registerButton = new TextButton("Register", skin);
        registerButton.getLabel().setFontScale(2f);
        Label statusLabel = new Label("", skin);
        statusLabel.setFontScale(2f);

        // Register button onclick logic
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String email = emailField.getText();
                String password = passwordField.getText();

                if (!email.isEmpty() && !password.isEmpty()) {
                    authService.signUp(email, password);
                    statusLabel.setText("Registration Successful!");

                    game.setScreen(new LoginView(game, authService));
                } else {
                    statusLabel.setText("Please enter valid credentials.");
                }
            }
        });

        // Arrange UI elements in the table with increased padding
        table.add(emailLabel).left().pad(20);
        table.add(emailField).width(400).height(100).pad(20);
        table.row();
        table.add(passwordLabel).left().pad(20);
        table.add(passwordField).width(400).height(100).pad(20);
        table.row();
        table.add(registerButton).colspan(2).center().pad(30).width(300).height(120);
        table.row();
        table.add(statusLabel).colspan(2).center().pad(20);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
    }
}
