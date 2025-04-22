package io.github.progark.Client.Views.Login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.progark.Client.Controllers.RegistrationController;
import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;

/*
 * RegistrationView.java
 * This class is responsible for displaying the registration view in the application.
 * It handles the rendering of the registration screen, including the logo, background, and input fields.
 * It also manages user interactions with the registration and back buttons.
 */
public class RegistrationView extends View {
    private RegistrationController controller;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Image background;
    private final Image logo;

    public RegistrationView(RegistrationController controller) {
        super();
        this.controller = controller;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        this.backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        this.logoTexture = new Texture(Gdx.files.internal("ThinkFastLogo.png"));

        this.background = new Image(backgroundTexture);
        this.logo = new Image(logoTexture);
    }

    @Override
    protected void initialize() {
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.add(logo).size(600, 600).center().padLeft(150).padBottom(20).row();

        Label titleLabel = new Label("Create Account", skin);
        titleLabel.setFontScale(4f);
        table.add(titleLabel).colspan(2).expandX().center().padBottom(30).row();

        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        usernameField.setSize(500, 100);
        usernameField.getStyle().font.getData().setScale(2f);
        table.add(usernameField).colspan(2).width(500).height(100).padBottom(20).row();

        TextField emailField = new TextField("", skin);
        emailField.setMessageText("Email");
        emailField.setSize(500, 100);
        emailField.getStyle().font.getData().setScale(2f);
        table.add(emailField).colspan(2).width(500).height(100).padBottom(20).row();

        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setSize(500, 100);
        passwordField.getStyle().font.getData().setScale(2f);
        table.add(passwordField).colspan(2).width(500).height(110).padBottom(30).row();

        TextButton registerButton = new TextButton("Register", skin);
        registerButton.getLabel().setFontScale(2f);
        registerButton.setSize(500, 120);
        registerButton.getStyle().font.getData().setScale(2f);
        table.add(registerButton).colspan(2).width(500).height(100).row();


        TextButton backButton = new TextButton("Back to Login", skin);
        backButton.getLabel().setFontScale(2f);
        backButton.setSize(400, 80);
        TextButton.TextButtonStyle clearStyle = new TextButton.TextButtonStyle(backButton.getStyle());
        clearStyle.up = null;
        clearStyle.down = null;
        clearStyle.over = null;
        backButton.setStyle(clearStyle);
        table.add(backButton).colspan(2).width(400).height(80).padTop(20).row();

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String email = emailField.getText();
                String password = passwordField.getText();
                String username = usernameField.getText();

                if (!email.isEmpty() && !password.isEmpty() && !username.isEmpty()) {
                        controller.registerUser(email, password, username);
                    } else {
                        System.out.println("Please enter a username");
                    }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.viewLoginPage();
            }
        });}

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        logoTexture.dispose();
    }
}
