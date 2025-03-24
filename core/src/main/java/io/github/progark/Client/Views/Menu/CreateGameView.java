package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.progark.Main;

import java.util.Random;

public class CreateGameView implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Texture startGameButtonTexture;
    private Image background;
    private String generatedPin;
    private BitmapFont bigFont;

    public CreateGameView(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Generer tilfeldig PIN
        generatedPin = String.format("%04d", new Random().nextInt(10000));

        // Bakgrunn
        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Last inn stor font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100; // Ønsket størrelse
        bigFont = generator.generateFont(parameter);
        generator.dispose();

        // Label-style med stor font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bigFont;
        labelStyle.fontColor = Color.BLACK;

        Label titleLabel = new Label("Your game pin:", labelStyle);
        Label pinLabel = new Label(generatedPin, labelStyle);

        // Start Game-knapp

        startGameButtonTexture = new Texture(Gdx.files.internal("StartGameButton.png"));
        TextureRegionDrawable startGameDrawable = new TextureRegionDrawable(new TextureRegion(startGameButtonTexture));
        ImageButton startGameButton = new ImageButton(startGameDrawable);

        startGameButton.getImage().setScaling(Scaling.stretch);         // Strekk bildet så det fyller
        startGameButton.getImage().setSize(3000, 600);     // Sett eksplisitt størrelse
        startGameButton.setSize(3000, 600);
        startGameButton.invalidateHierarchy();                          // Oppdater layout

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.center().padTop(200);
        stage.addActor(table);

        table.add(titleLabel).padBottom(20).row();
        table.add(pinLabel).padBottom(40).row();
        table.add(startGameButton).size(3000,600);
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

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        startGameButtonTexture.dispose();
        bigFont.dispose();
    }
}
