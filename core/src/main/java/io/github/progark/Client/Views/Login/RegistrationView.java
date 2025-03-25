package io.github.progark.Client.Views.Login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.progark.Client.Views.Menu.LandingView;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;

public class RegistrationView implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private AuthService authService;
    private DataCallback callback;

    private Texture backgroundTexture;
    private Texture logoTexture;
    private Image background;
    private Image logo;



    public RegistrationView(Main game, AuthService authService) {
        this.game = game;
        this.authService = authService;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Ensure the file exists

        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        logoTexture = new Texture(Gdx.files.internal("ThinkFastLogo 2.png"));

        background = new Image(backgroundTexture);
        logo = new Image(logoTexture);

        background.setFillParent(true);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(background);
        stage.addActor(table);

        table.add(logo).colspan(2).expandX().center().padBottom(100).row();

        Label titleLabel = new Label("Sign up", skin);
        titleLabel.setFontScale(2.5f);
        table.add(titleLabel).colspan(2).expandX().center().padBottom(30).row();


        // Create UI elements with increased size
        Label usernameLabel = new Label("Username:", skin);
        usernameLabel.setFontScale(2f);
        TextField usernameField = new TextField("", skin);
        usernameField.setStyle(usernameField.getStyle());
        usernameField.getStyle().font.getData().setScale(2f);

        Label emailLabel = new Label("Email:", skin);
        emailLabel.setFontScale(2f);
        TextField emailField = new TextField("", skin);
        emailField.setStyle(emailField.getStyle());
        emailField.getStyle().font.getData().setScale(2f);

        Label passwordLabel = new Label("Password:", skin);
        passwordLabel.setFontScale(2f);
        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.getStyle().font.getData().setScale(2f);

        Label confirmPasswordLabel = new Label("Confirm password:", skin);
        confirmPasswordLabel.setFontScale(2f);
        TextField confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        confirmPasswordField.getStyle().font.getData().setScale(2f);

        TextButton registerButton = new TextButton("Register", skin);
        registerButton.getLabel().setFontScale(2f);
        Label statusLabel = new Label("", skin);
        statusLabel.setFontScale(2f);

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String email = emailField.getText();
                String password = passwordField.getText();
                String username = usernameField.getText();

                if (!email.isEmpty() && !password.isEmpty()) {
                    authService.signUp(email, password, username, callback);
                    statusLabel.setText("Registration Successful!");

                    game.setScreen(new LandingView(game));
                } else {
                    statusLabel.setText("Please enter valid credentials.");
                }
            }
        });

        // Arrange UI elements in the table with increased padding
        table.add(usernameLabel).expandX().left().pad(20);
        table.add(usernameField).width(400).padLeft(60).height(100).pad(20);
        table.row();
        table.add(emailLabel).left().padLeft(60).pad(20);
        table.add(emailField).width(400).padLeft(60).height(100).pad(20);
        table.row();
        table.add(passwordLabel).left().padLeft(60).pad(20);
        table.add(passwordField).width(400).padLeft(60).height(100).pad(20);
        table.row();
        table.add(confirmPasswordLabel).left().padLeft(60).pad(20);
        table.add(confirmPasswordField).width(400).padLeft(60).height(100).pad(20);
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
        backgroundTexture.dispose();
        logoTexture.dispose();
    }
}
