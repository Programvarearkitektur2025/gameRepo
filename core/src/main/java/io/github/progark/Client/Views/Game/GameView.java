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

import io.github.progark.Client.Views.View;
import io.github.progark.Main;

public class GameView extends View {

    private final Main game;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture enterButtonTexture;
    private final Image background;

    private final BitmapFont font;

    public GameView(Main game) {
        super();
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        this.backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        this.enterButtonTexture = new Texture(Gdx.files.internal("Enter.png"));
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
        table.center().padTop(50);
        stage.addActor(table);

        // Timer
        Label timerLabel = new Label("27 sec", new Label.LabelStyle(font, Color.WHITE));
        table.add(timerLabel).padBottom(50).row();

        // Prompt
        Label promptLabel = new Label("Girlsname starting with L", new Label.LabelStyle(font, Color.WHITE));
        table.add(promptLabel).padBottom(60).row();

        // Style uten skygge
        TextField.TextFieldStyle flatWhiteStyle = new TextField.TextFieldStyle();
        flatWhiteStyle.font = font;
        flatWhiteStyle.fontColor = Color.BLACK;
        flatWhiteStyle.background = skin.newDrawable("white", Color.WHITE);
        flatWhiteStyle.cursor = skin.newDrawable("white", Color.GRAY);

        // Meldingsboks (over input)
        TextArea messageBox = new TextArea("", flatWhiteStyle);
        messageBox.setDisabled(true);
        table.add(messageBox).width(800).height(1000).padBottom(80).row();

        // Svarfelt og knapp på rad
        TextField answerField = new TextField("", flatWhiteStyle);
        answerField.setMessageText("Write your answer");

        ImageButton enterButton = new ImageButton(new TextureRegionDrawable(enterButtonTexture));
        enterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Entered: " + answerField.getText());
            }
        });

        Table inputRow = new Table();
        inputRow.add(answerField).width(500).height(130).padRight(15);
        inputRow.add(enterButton).size(265,150).padLeft(15);
        table.add(inputRow);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        enterButtonTexture.dispose();
        font.dispose();
    }
}
