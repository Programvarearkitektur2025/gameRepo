package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    private final Texture backgroundTexture, profileTexture, startGameTexture, textFieldTexture, waitingForOpponentTexture, gamePinTexture, backButtonTexture;
    private final BitmapFont font;
    private final GameController controller;

    public GameView(GameController controller){
        super();
        this.controller = controller;

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        this.profileTexture = new Texture(Gdx.files.internal("Person.png"));
        this.textFieldTexture = new Texture(Gdx.files.internal("TextField.png"));
        this.startGameTexture = new Texture(Gdx.files.internal("Start_Game_Button.png"));
        this.waitingForOpponentTexture = new Texture(Gdx.files.internal("Waiting_For_Opponent.png"));
        this.gamePinTexture = new Texture(Gdx.files.internal("Game_Pin.png"));
        this.backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    protected void initialize() {
        // Bakgrunn
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

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

        // Headline
        String headlineText;
        if (controller.isMultiplayer()) {
            headlineText = "Waiting for opponent" + "Game pin:" + controller.getLobbyCode();
        } else {
            headlineText = "Press Start Game to play";
        }
        Label headline = new Label(headlineText, new Label.LabelStyle(font, Color.WHITE));
        headline.setFontScale(2.5f);
        table.add(headline).padBottom(50).padTop(150).row();
        stage.addActor(headline);

        // Profilbilder og score
        Table scoreRow = new Table();
        Image leftProfile = new Image(profileTexture);
        Image rightProfile = new Image(profileTexture);
        Label scoreLabel = new Label(controller.getPlayerOnePoints() + " - " + controller.getPlayerTwoPoints(), new Label.LabelStyle(font, Color.WHITE));
        scoreRow.add(leftProfile).size(120).padRight(40);
        scoreRow.add(scoreLabel).padRight(40);
        scoreRow.add(rightProfile).size(120);
        table.add(scoreRow).padTop(60).padBottom(100).row();
        stage.addActor(scoreRow);

        // Resultatboks med prompt og svar (bruker TextField.png som bakgrunn)
        Drawable textFieldBackground = new TextureRegionDrawable(textFieldTexture);
        Image boxBackground = new Image(textFieldBackground);
        Stack resultBox = new Stack();
        resultBox.add(boxBackground);
        Table resultTable = new Table();
        resultTable.top().padTop(30);
        Label promptLabel = new Label("Girlsname starting with ‘L’", new Label.LabelStyle(font, Color.DARK_GRAY));
        promptLabel.setAlignment(Align.center);
        resultTable.add(promptLabel).padBottom(30).row();
        resultBox.add(resultTable);
        table.add(resultBox).width(850).height(1050).padBottom(40).row();
        stage.addActor(resultBox);

        // Start Game knapp
        ImageButton startGameButton = new ImageButton(new TextureRegionDrawable(startGameTexture));
        startGameButton.setSize(800, 300);
        startGameButton.setPosition(Gdx.graphics.getWidth() / 2 - startGameButton.getWidth() / 2, 100); // Adjust as needed
        // Disable the button if multiplayer and second player hasn't joined
        if (controller.isMultiplayer()) {
            // Check if second player has joined the game
            if (controller.getPlayerTwo() == null || controller.getPlayerTwo().isEmpty()) {
                startGameButton.setDisabled(true);  // Disable the button
            } else {
                startGameButton.setDisabled(false);  // Enable the button
            }
        } else {
            // Enable the button in single player mode
            startGameButton.setDisabled(false);  // Always enabled for single player
        }
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!startGameButton.isDisabled()) {
                    controller.goToRound();
                }
            }
        });
        stage.addActor(startGameButton);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        font.dispose();
        backgroundTexture.dispose();
        profileTexture.dispose();
        startGameTexture.dispose();
        textFieldTexture.dispose();
        waitingForOpponentTexture.dispose();
        gamePinTexture.dispose();
    }
}

