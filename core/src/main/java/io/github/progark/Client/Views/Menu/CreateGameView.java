package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.function.IntConsumer;

import io.github.progark.Client.Controllers.CreateGameController;
import io.github.progark.Client.Views.View;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.database.DataCallback;
/*
 * CreateGameView.java
 * This class is responsible for displaying the create game view in the application.
 * It handles the rendering of the game creation screen, including the background, buttons, and labels.
 * It also manages user interactions with the game mode, difficulty, and rounds selection.
 * The view interacts with the CreateGameController to perform operations related to game creation.
 * The view is designed to be user-friendly and visually appealing, with a focus on providing a smooth user experience.
 * The view uses textures for buttons and labels, and it employs a table layout for organizing UI elements.
 */
public class CreateGameView extends View {

    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture startGameButtonTexture;
    private final Texture backButtonTexture;
    private final Texture roundsToPlayTextTexture;
    private final Image background;
    private final BitmapFont font;

    private Texture singleGrey, singleWhite, multiGrey, multiWhite;
    private Texture easyGrey, easyWhite, mediumGrey, mediumWhite, hardGrey, hardWhite;
    private Texture oneGrey, oneWhite, twoGrey, twoWhite, threeGrey, threeWhite;

    private int selectedMode = 0;
    private int selectedDifficulty = 0;
    private int selectedRounds = 0;

    private final CreateGameController controller;

    public CreateGameView(CreateGameController createGameController) {
        super();
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.controller = createGameController;

        this.backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        this.startGameButtonTexture = new Texture(Gdx.files.internal("CreateGreen.png"));
        this.backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));
        this.roundsToPlayTextTexture = new Texture(Gdx.files.internal("RoundsToPlayText.png"));
        this.background = new Image(backgroundTexture);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    protected void initialize() {
        background.setFillParent(true);
        stage.addActor(background);

        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setPosition(30, Gdx.graphics.getHeight() - 100); // Adjust position
        backButton.setSize(80, 80);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goBackToHome();
            }
        });
        stage.addActor(backButton);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(100);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label titleLabel = new Label("Create a game", labelStyle);
        titleLabel.setAlignment(Align.center);
        table.add(titleLabel).colspan(3).padBottom(150).padTop(150).center().row();

        loadTextures();

        // --- Game Mode Buttons ---
        ImageButton singleBtn = new ImageButton(new TextureRegionDrawable(singleGrey));
        ImageButton multiBtn = new ImageButton(new TextureRegionDrawable(multiGrey));
        ImageButton[] modeButtons = {singleBtn, multiBtn};
        Texture[] modeWhite = {singleWhite, multiWhite};
        Texture[] modeGrey = {this.singleGrey, this.multiGrey};
        addToggleLogic(modeButtons, modeWhite, modeGrey, i -> selectedMode = i);

        Table modeRow = new Table();
        modeRow.add(singleBtn).pad(20).width(300).height(100);
        modeRow.add(multiBtn).pad(20).width(300).height(100);
        table.add(modeRow).colspan(3).padBottom(100).row();

        // --- Difficulty Buttons ---
        ImageButton easyBtn = new ImageButton(new TextureRegionDrawable(easyGrey));
        ImageButton mediumBtn = new ImageButton(new TextureRegionDrawable(mediumGrey));
        ImageButton hardBtn = new ImageButton(new TextureRegionDrawable(hardGrey));
        ImageButton[] diffButtons = {easyBtn, mediumBtn, hardBtn};
        Texture[] diffWhite = {easyWhite, mediumWhite, hardWhite};
        Texture[] diffGrey = {this.easyGrey, this.mediumGrey, this.hardGrey};
        addToggleLogic(diffButtons, diffWhite, diffGrey, i -> selectedDifficulty = i);

        Table diffRow = new Table();
        diffRow.add(easyBtn).pad(10).width(200).height(100);
        diffRow.add(mediumBtn).pad(10).width(200).height(100);
        diffRow.add(hardBtn).pad(10).width(200).height(100);
        table.add(diffRow).colspan(3).padBottom(100).row();

        // --- Rounds Label ---
        Image roundsToPlayText = new Image(roundsToPlayTextTexture);
        table.add(roundsToPlayText).padBottom(60).width(900).height(70).center().row();

        // --- Rounds Buttons ---
        ImageButton oneBtn = new ImageButton(new TextureRegionDrawable(oneGrey));
        ImageButton twoBtn = new ImageButton(new TextureRegionDrawable(twoGrey));
        ImageButton threeBtn = new ImageButton(new TextureRegionDrawable(threeGrey));
        ImageButton[] roundButtons = {oneBtn, twoBtn, threeBtn};
        Texture[] roundWhite = {oneWhite, twoWhite, threeWhite};
        Texture[] roundGrey = {oneGrey, twoGrey, threeGrey};
        addToggleLogic(roundButtons, roundWhite, roundGrey, i -> selectedRounds = i);

        Table roundsRow = new Table();
        roundsRow.add(oneBtn).pad(10).width(200).height(100);
        roundsRow.add(twoBtn).pad(10).width(200).height(100);
        roundsRow.add(threeBtn).pad(10).width(200).height(100);
        table.add(roundsRow).colspan(3).padBottom(100).row();

        // --- Start Game Button ---
        ImageButton startGameButton = new ImageButton(new TextureRegionDrawable(startGameButtonTexture));
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int rounds = selectedRounds + 1;
                boolean isMultiplayer = selectedMode == 1;
                int difficulty = selectedDifficulty + 1;
                controller.createLobby(difficulty, rounds, isMultiplayer, new DataCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        GameModel ourGame = (GameModel) data;
                        controller.viewGamePage(ourGame);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Could not create game: " + e);
                    }
                });
            }
        });

        table.add(startGameButton).colspan(3).center().width(700).height(400);
    }

    private void loadTextures() {
        // Mode
        singleGrey = new Texture("SinglePlayerGrey.png");
        singleWhite = new Texture("SingleplayerWhite.png");
        multiGrey = new Texture("MultiplayerGray.png");
        multiWhite = new Texture("MultiplayerWhite.png");

        // Difficulty
        easyGrey = new Texture("EasyGrey.png");
        easyWhite = new Texture("EasyWhite.png");
        mediumGrey = new Texture("MediumGrey.png");
        mediumWhite = new Texture("MediumWhite.png");
        hardGrey = new Texture("HardGrey.png");
        hardWhite = new Texture("HardWhite.png");

        // Rounds
        oneGrey = new Texture("OneRoundGray.png");
        oneWhite = new Texture("OneRoundWhite.png");
        twoGrey = new Texture("TwoRoundsGray.png");
        twoWhite = new Texture("TwoRoundsWhite.png");
        threeGrey = new Texture("ThreeRoundsGray.png");
        threeWhite = new Texture("ThreeRoundsWhite.png");
    }

    /*
     * addToggleLogic
     * This method adds toggle logic to the provided buttons.
     * It sets up click listeners for each button to change their appearance
     * and update the selected index.
     * The onSelected consumer is called with the index of the selected button.
     */
    private void addToggleLogic(ImageButton[] buttons, Texture[] whiteTextures, Texture[] greyTextures, IntConsumer onSelected) {
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    for (int j = 0; j < buttons.length; j++) {
                        TextureRegionDrawable drawable = new TextureRegionDrawable(
                            j == index ? whiteTextures[j] : greyTextures[j]
                        );
                        buttons[j].getStyle().imageUp = drawable;
                    }
                    onSelected.accept(index);
                }
            });
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        startGameButtonTexture.dispose();
        backButtonTexture.dispose();
        roundsToPlayTextTexture.dispose();
        singleGrey.dispose();
        singleWhite.dispose();
        multiGrey.dispose();
        multiWhite.dispose();
        easyGrey.dispose();
        easyWhite.dispose();
        mediumGrey.dispose();
        mediumWhite.dispose();
        hardGrey.dispose();
        hardWhite.dispose();
        oneGrey.dispose();
        oneWhite.dispose();
        twoGrey.dispose();
        twoWhite.dispose();
        threeGrey.dispose();
        threeWhite.dispose();
        font.dispose();
    }
}
