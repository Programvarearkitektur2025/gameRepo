package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import io.github.progark.Client.Controllers.GameController;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;

public class GameView extends View {

    private final Skin skin;
    private final Texture backgroundTexture, profileTexture, startGameTexture, textFieldTexture, backButtonTexture, startGameDisabledTexture;
    private final BitmapFont smallFont, largerFont;
    private final GameController controller;
    private Image background;

    public GameView(GameController controller){
        super();
        this.controller = controller;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        profileTexture = new Texture(Gdx.files.internal("Person.png"));
        textFieldTexture = new Texture(Gdx.files.internal("TextField.png"));
        startGameTexture = new Texture(Gdx.files.internal("Start_Game_Button.png"));
        startGameDisabledTexture = new Texture(Gdx.files.internal("Start_Game_Button_Disabled.png"));
        backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        // Small font
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        this.smallFont = generator.generateFont(parameter);
        // Larger font
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 30;
        this.largerFont = generator.generateFont(parameter2);

        generator.dispose();

        background = new Image(backgroundTexture);

        enter();
    }

    @Override
    protected void initialize() {

        // Bakgrunn
        background.setFillParent(true);
        stage.addActor(background);
        System.out.println("Hello from gameView");

        // Tilbake knapp
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(100, 100);
        backButton.setPosition(30, Gdx.graphics.getHeight() - 130);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goToHome();
            }
        });
        stage.addActor(backButton);

        // Tabell
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(40);
        stage.addActor(table);

        if (controller.isMultiplayer()) {
            // Headline
            String headlineText = "Game Pin: " + controller.getLobbyCode();
            Label headline = new Label(headlineText, new Label.LabelStyle(largerFont, skin.getColor("white")));
            headline.setFontScale(2.5f);
            table.add(headline).padBottom(50).padTop(150).row();
            headline.setPosition(230, Gdx.graphics.getHeight() - 250);
            stage.addActor(headline);

            // Profilbilder og score
            Table scoreRow = new Table();

            // Left player (profile + name)
            Table leftProfileCol = new Table();
            Image leftProfile = new Image(profileTexture);
            Label leftNameLabel = new Label(controller.getPlayerOne(), new Label.LabelStyle(smallFont, skin.getColor("white")));
            leftNameLabel.setAlignment(Align.center);
            leftNameLabel.setFontScale(2f);
            leftProfileCol.add(leftProfile).size(250).row();
            leftProfileCol.add(leftNameLabel).padTop(40).width(250).center();

            // Right player (profile + name)
            Table rightProfileCol = new Table();
            Image rightProfile = new Image(profileTexture);
            Label rightNameLabel = new Label(controller.getPlayerTwo(), new Label.LabelStyle(smallFont, skin.getColor("white")));
            rightNameLabel.setAlignment(Align.center);
            rightNameLabel.setFontScale(2f);
            rightProfileCol.add(rightProfile).size(250).row();
            rightProfileCol.add(rightNameLabel).padTop(40).width(250).center();

            // Score label
            Label scoreLabel = new Label(controller.getPlayerOnePoints() + " - " + controller.getPlayerTwoPoints(), new Label.LabelStyle(largerFont, skin.getColor("white")));
            scoreLabel.setFontScale(2f);

            // Add to horizontal score row
            scoreRow.add(leftProfileCol).padRight(80);
            scoreRow.add(scoreLabel).padRight(80).center();
            scoreRow.add(rightProfileCol);

            // Position the row manually
            scoreRow.setPosition(530, Gdx.graphics.getHeight() - 500);
            stage.addActor(scoreRow);

        } else {
            // Headline
            String headlineText = "Press Start Game to play";
            Label headline = new Label(headlineText, new Label.LabelStyle(largerFont, skin.getColor("white")));
            headline.setFontScale(2.5f);
            table.add(headline).padBottom(50).padTop(150).row();
            headline.setPosition(120, Gdx.graphics.getHeight() - 250);
            stage.addActor(headline);

            // Profilbilde med navn
            Table scoreRow = new Table();
            Table profileCol = new Table();
            Image profile = new Image(profileTexture);
            Label nameLabel = new Label(controller.getPlayerOne(), new Label.LabelStyle(smallFont, skin.getColor("white")));
            nameLabel.setAlignment(Align.center);
            nameLabel.setFontScale(2f);
            profileCol.add(profile).size(250).row();
            profileCol.add(nameLabel).padTop(40).width(250).center();
            scoreRow.add(profileCol).padRight(80);
            scoreRow.setPosition(580, Gdx.graphics.getHeight() - 500);
            stage.addActor(scoreRow);

        }

        // Background for result box
        Drawable textFieldBackground = new TextureRegionDrawable(textFieldTexture);
        Image boxBackground = new Image(textFieldBackground);
        Table resultTable = new Table();
        resultTable.top().padTop(30);
        // ScrollPane to make the content scrollable
        ScrollPane scrollPane = new ScrollPane(resultTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        Stack resultBox = new Stack();
        resultBox.add(boxBackground);
        resultBox.add(scrollPane);
        resultBox.setSize(850f, 1200f);
        resultBox.setPosition((Gdx.graphics.getWidth() - 850f) / 2f, 450f);
        stage.addActor(resultBox);

        // Start Game Button, disabled and active
        TextureRegionDrawable startGameUp = new TextureRegionDrawable(startGameTexture);
        TextureRegionDrawable startGameDisabled = new TextureRegionDrawable(startGameDisabledTexture);
        Button.ButtonStyle startGameStyle = new Button.ButtonStyle();
        startGameStyle.up = startGameUp;
        startGameStyle.disabled = startGameDisabled;
        Button startGame = new Button(startGameStyle);
        startGame.setSize(850f, 200);
        startGame.setPosition(Gdx.graphics.getWidth() / 2 - startGame.getWidth() / 2, 200);
        if (controller.isMultiplayer()) {
            boolean playerTwoJoined = controller.getPlayerTwo() != null && !controller.getPlayerTwo().isEmpty();
            startGame.setDisabled(!playerTwoJoined);
        } else {
            startGame.setDisabled(false);
        }

        // Add listener for the button
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!startGame.isDisabled()) {
                    controller.goToRound();
                }
            }
        });

        // Add button to stage
        stage.addActor(startGame);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        backButtonTexture.dispose();
        skin.dispose();
        smallFont.dispose();
        largerFont.dispose();
        profileTexture.dispose();
        startGameTexture.dispose();
        textFieldTexture.dispose();
    }
}

