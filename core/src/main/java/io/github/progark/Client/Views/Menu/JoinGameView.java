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

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;





public class JoinGameView implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Texture joinButtonTexture;
    private Image background;

    public JoinGameView(Main game) {
        this.game = game;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Bakgrunn
        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Join-knapp som bilde
        joinButtonTexture = new Texture(Gdx.files.internal("JoinButton.png"));
        TextureRegionDrawable joinDrawable = new TextureRegionDrawable(new TextureRegion(joinButtonTexture));
        ImageButton joinButton = new ImageButton(joinDrawable);

        // Tekstfelt og label
        Label enterPinLabel = new Label("Enter game pin:", skin);
        enterPinLabel.setColor(Color.BLACK);
        TextField pinField = new TextField("", skin);
        pinField.setMessageText("Game PIN");


        // Layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(enterPinLabel).padBottom(20).row();
        table.add(pinField).width(300).height(60).padBottom(30).row();
        table.add(joinButton).width(300).height(70);
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
        joinButtonTexture.dispose();
    }
}

