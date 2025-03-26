package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import io.github.progark.Client.Views.View;
import io.github.progark.Main;

public class GameView extends View {

    private final Main game;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture enterButtonTexture;
    private final Texture textFieldTexture;
    private final Texture rectangleTexture;
    private final Image background;

    private final BitmapFont font;

    public GameView(Main game) {
        super();
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        this.backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        this.enterButtonTexture = new Texture(Gdx.files.internal("Enter.png"));
        this.textFieldTexture = new Texture(Gdx.files.internal("TextField.png"));
        this.rectangleTexture = new Texture(Gdx.files.internal("Rectangle.png"));
        this.background = new Image(backgroundTexture);

        // Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    protected void initialize() {
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);
        stage.addActor(table);

        // Timer
        Label timerLabel = new Label("TIME LEFT: 27 sec", new Label.LabelStyle(font, Color.WHITE));
        table.add(timerLabel).padBottom(50).row();

        // === Meldingsboks med midtstilt kategori ===

        // Bruk TextField.png som bakgrunn for meldingsboks
        Drawable messageBackgroundDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("TextField.png")));

        // Kategoritekst (midtstilt)
        Label promptLabel = new Label("Girl name starting with L", new Label.LabelStyle(font, Color.DARK_GRAY));
        promptLabel.setAlignment(Align.center);

        // Pakk inn i bord for å sentrere teksten i toppen av bildet
        Table promptTable = new Table();
        promptTable.setFillParent(true);
        promptTable.top().padTop(25);
        promptTable.add(promptLabel).expandX().center();

        // Stack for å kombinere bilde og tekst
        Stack messageStack = new Stack();
        Image messageBackground = new Image(messageBackgroundDrawable);
        messageStack.add(messageBackground);
        messageStack.add(promptTable);

        table.add(messageStack).width(800).height(1050).padBottom(60).row();

        // === Tekstinput med Rectangle.png ===

        Drawable inputBackgroundDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("Rectangle.png")));

        TextField.TextFieldStyle inputStyle = new TextField.TextFieldStyle();
        inputStyle.font = font;
        inputStyle.fontColor = Color.DARK_GRAY;
        inputStyle.background = inputBackgroundDrawable;
        inputStyle.cursor = skin.newDrawable("white", Color.GRAY);

        TextField answerField = new TextField("", inputStyle);
        answerField.setMessageText("  Write your answer");

        // Enter-knapp
        ImageButton enterButton = new ImageButton(new TextureRegionDrawable(enterButtonTexture));
        enterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Entered: " + answerField.getText());
            }
        });

        // Raden med inputfelt og knapp
        Table inputRow = new Table();
        inputRow.add(answerField).width(500).height(130).padRight(15);
        inputRow.add(enterButton).size(265, 150).padLeft(15);
        table.add(inputRow);
    }



    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        enterButtonTexture.dispose();
        rectangleTexture.dispose();
        textFieldTexture.dispose();
        font.dispose();
    }
}
