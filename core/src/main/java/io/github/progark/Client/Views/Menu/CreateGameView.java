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

import java.util.Random;

import io.github.progark.Client.Controllers.CreateGameController;
import io.github.progark.Client.Views.View;

public class CreateGameView extends View {
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture startGameButtonTexture;
    private final Image background;
    private final BitmapFont font;

    // Textures
    private Texture singleGrey, singleWhite, multiGrey, multiWhite;
    private Texture easyGrey, easyWhite, mediumGrey, mediumWhite, hardGrey, hardWhite;
    private Texture oneGrey, oneWhite, twoGrey, twoWhite, threeGrey, threeWhite;

    // State
    private int selectedMode = 0;         // 0 = Single, 1 = Multi
    private int selectedDifficulty = 0;   // 0 = Easy, 1 = Medium, 2 = Hard
    private int selectedRounds = 0;       // 0 = 1, 1 = 2, 2 = 3

    private CreateGameController controller;

    public CreateGameView(CreateGameController createGameController) {
        super();
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.controller = createGameController;

        this.backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        this.startGameButtonTexture = new Texture(Gdx.files.internal("StartGameButton.png"));

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

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(80);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label titleLabel = new Label("Create a game", labelStyle);
        titleLabel.setAlignment(Align.center);
        table.add(titleLabel).colspan(3).padBottom(40).row();

        loadTextures();

        // Mode buttons
        ImageButton singleBtn = new ImageButton(new TextureRegionDrawable(singleGrey));
        ImageButton multiBtn = new ImageButton(new TextureRegionDrawable(multiGrey));
        ImageButton[] modeButtons = {singleBtn, multiBtn};
        Texture[] modeWhite = {singleWhite, multiWhite};
        Texture[] modeGrey = {this.singleGrey, this.multiGrey};
        addToggleLogic(modeButtons, modeWhite, modeGrey, i -> selectedMode = i);
        table.add(singleBtn).pad(10);
        table.add(multiBtn).pad(10).row();

        // Difficulty buttons
        ImageButton easyBtn = new ImageButton(new TextureRegionDrawable(easyGrey));
        ImageButton mediumBtn = new ImageButton(new TextureRegionDrawable(mediumGrey));
        ImageButton hardBtn = new ImageButton(new TextureRegionDrawable(hardGrey));
        ImageButton[] diffButtons = {easyBtn, mediumBtn, hardBtn};
        Texture[] diffWhite = {easyWhite, mediumWhite, hardWhite};
        Texture[] diffGrey = {this.easyGrey, this.mediumGrey, this.hardGrey};
        addToggleLogic(diffButtons, diffWhite, diffGrey, i -> selectedDifficulty = i);
        table.add(easyBtn).pad(10);
        table.add(mediumBtn).pad(10);
        table.add(hardBtn).pad(10).row();

        // Rounds buttons
        ImageButton oneBtn = new ImageButton(new TextureRegionDrawable(oneGrey));
        ImageButton twoBtn = new ImageButton(new TextureRegionDrawable(twoGrey));
        ImageButton threeBtn = new ImageButton(new TextureRegionDrawable(threeGrey));
        ImageButton[] roundButtons = {oneBtn, twoBtn, threeBtn};
        Texture[] roundWhite = {oneWhite, twoWhite, threeWhite};
        Texture[] roundGrey = {oneGrey, twoGrey, threeGrey};
        addToggleLogic(roundButtons, roundWhite, roundGrey, i -> selectedRounds = i);
        table.add(oneBtn).pad(10);
        table.add(twoBtn).pad(10);
        table.add(threeBtn).pad(10).row();

        // Create button
        ImageButton startGameButton = new ImageButton(new TextureRegionDrawable(startGameButtonTexture));
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int rounds = selectedRounds + 1; // 0 => 1 round, etc.
                boolean isMultiplayer = selectedMode == 1;
                controller.createLobby(selectedDifficulty, rounds, isMultiplayer);
                System.out.println("Start game clicked - Mode: " + isMultiplayer + ", Diff: " + selectedDifficulty + ", Rounds: " + rounds);
            }
        });

        table.add(startGameButton).colspan(3).padTop(40).width(600).height(180).row();
    }

    private void loadTextures() {
        // Mode
        singleGrey = new Texture("SingleplayerGrey.png");
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

    private void addToggleLogic(ImageButton[] buttons, Texture[] whiteTextures, Texture[] greyTextures, java.util.function.IntConsumer onSelected) {
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
        font.dispose();
    }
}
