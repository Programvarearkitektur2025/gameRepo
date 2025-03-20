package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import io.github.progark.Client.Views.Login.LoginView;
import io.github.progark.Client.Views.Login.RegistrationView;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;

public class HomeView implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Texture logoTexture;
    private Image background;
    private Image logo;

    public HomeView(Main game) {
        this.game = game;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        logoTexture = new Texture(Gdx.files.internal("ThinkFastLogo.png"));

        background = new Image(backgroundTexture);
        logo = new Image(logoTexture);

        background.setFillParent(true);


        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(background);
        stage.addActor(table);

        table.add(logo).padBottom(100).row(); //padding between the logo and buttons

        // Create a buttons
        //TextButton loginButton = new TextButton("LOG IN", skin,"ninepatch");
        //TextButton signUpButton = new TextButton("SIGN UP", skin, "ninepatch");

        TextButton loginButton = new TextButton("LOG IN", skin,"ninepatch");
        TextButton signUpButton = new TextButton("SIGN UP", skin, "ninepatch");

        // Style the buttons
        loginButton.getLabel().setFontScale(2f);
        signUpButton.getLabel().setFontScale(2f);

        // Add buttons to table
        table.add(loginButton).width(300).height(80).padBottom(30).row();
        table.add(signUpButton).width(300).height(80);

        // Add event listeners
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoginView(game));
            }
        });

        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new RegistrationView(game, null)); //Have set null for now, to check
            }
        });



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
