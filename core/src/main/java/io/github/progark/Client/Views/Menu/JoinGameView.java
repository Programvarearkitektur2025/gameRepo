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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.progark.Main;

public class JoinGameView implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Texture joinButtonTexture;
    private Image background;
    private BitmapFont bigFont;
    private BitmapFont smallFont;

    public JoinGameView(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Bakgrunn
        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Last inn font-generator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));

        // Stor font
        FreeTypeFontGenerator.FreeTypeFontParameter bigParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        bigParam.size = 80;
        bigFont = generator.generateFont(bigParam);

        // Mindre font for PIN
        FreeTypeFontGenerator.FreeTypeFontParameter smallParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallParam.size = 40;
        smallFont = generator.generateFont(smallParam);

        generator.dispose(); // ferdig med generator

        // Label-style med stor font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bigFont;
        labelStyle.fontColor = Color.BLACK;

        // Tekstfelt-style med mindre font
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = smallFont;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = skin.getDrawable("textfield");
        textFieldStyle.cursor = skin.getDrawable("cursor");
        textFieldStyle.selection = skin.getDrawable("selection");

        Label enterPinLabel = new Label("Enter game pin:", labelStyle);
        TextField pinField = new TextField("", textFieldStyle);
        pinField.setMessageText("Game PIN");
        pinField.setAlignment(1); // midtstilt tekst

        // Join-knapp som bilde
        joinButtonTexture = new Texture(Gdx.files.internal("JoinButton.png"));
        TextureRegionDrawable joinDrawable = new TextureRegionDrawable(new TextureRegion(joinButtonTexture));
        ImageButton joinButton = new ImageButton(joinDrawable);
        joinButton.getImage().setScaling(Scaling.stretch);
        joinButton.getImage().setSize(3000, 600);
        joinButton.setSize(3000, 600);
        joinButton.invalidateHierarchy();

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.center().padTop(200);
        stage.addActor(table);

        table.add(enterPinLabel).padBottom(60).row();
        table.add(pinField).width(550).height(100).padBottom(60).row();
        table.add(joinButton);
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
        joinButtonTexture.dispose();
        bigFont.dispose();
        smallFont.dispose();
    }
}
