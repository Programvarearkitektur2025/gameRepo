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
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.progark.Client.Controllers.GameController;
import io.github.progark.Client.Views.View;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.database.DataCallback;

public class GameView extends View {

    private final Skin skin;
    private final Texture backgroundTexture, nextRoundTexture, nextRoundDisabledTexture, playRoundTexture, showLeaderBoardTexture, profileTexture, startGameTexture, textFieldTexture, backButtonTexture, startGameDisabledTexture;
    private final BitmapFont smallFont, largerFont, roundFont, answerFont;
    private final GameController controller;
    private Image background;

    private int displayedRoundIndex = 0;
    private Table resultTable;
    private List<RoundModel> playedRounds;

    private boolean initialized = false;
    private Label headlineLabel;

    public GameView(GameController controller){
        super();
        this.controller = controller;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        nextRoundTexture = new Texture(Gdx.files.internal("nextRound.png"));
        nextRoundDisabledTexture = new Texture(Gdx.files.internal("nextRoundDisabled.png"));
        showLeaderBoardTexture = new Texture(Gdx.files.internal("showResults.png"));
        profileTexture = new Texture(Gdx.files.internal("Person.png"));
        textFieldTexture = new Texture(Gdx.files.internal("TextField.png"));
        startGameTexture = new Texture(Gdx.files.internal("Start_Game_Button.png"));
        startGameDisabledTexture = new Texture(Gdx.files.internal("Start_Game_Button_Disabled.png"));
        backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));
        playRoundTexture = new Texture(Gdx.files.internal("play round.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        this.smallFont = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 30;
        this.largerFont = generator.generateFont(parameter2);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = 36;
        this.roundFont = generator.generateFont(parameter3);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter4 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter4.size = 26;
        this.answerFont = generator.generateFont(parameter4);

        generator.dispose();

        background = new Image(backgroundTexture);

    }

    @Override
    protected void initialize() {
        if (initialized) return;

        if (controller.isMultiplayer()){
            initializeMultiplayer();
        }else {
            initializeSingleplayer();
        }
        initialized = true;
    }

    private void initializeMultiplayer() {
        commonInitialize();
        addGamePinHeadline();
        addPlayerScoreRow();
        initializePlayedRounds();
        addResultScrollBox();
        addNavigationButtons();
        if (!playedRounds.isEmpty()) showRoundResult(displayedRoundIndex);
        addPlayRoundButtonIfAvailable();
    }

    private void addGamePinHeadline() {
        String headlineText = "Game Pin: " + controller.getLobbyCode();
        headlineLabel = new Label(headlineText, new Label.LabelStyle(largerFont, skin.getColor("white")));
        headlineLabel.setFontScale(2.5f);
        headlineLabel.setPosition(120, Gdx.graphics.getHeight() - 250);
        stage.addActor(headlineLabel);
    }

    private void addPlayerScoreRow() {
        Table scoreRow = new Table();
        scoreRow.add(createPlayerProfile(controller.getPlayerOne())).padRight(80);
        scoreRow.add(createScoreLabel()).padRight(80).center();
        scoreRow.add(createPlayerProfile(controller.getPlayerTwo()));
        scoreRow.setPosition(530, Gdx.graphics.getHeight() - 500);
        stage.addActor(scoreRow);
    }

    private Table createPlayerProfile(String playerName) {
        Table profileCol = new Table();
        Image profileImage = new Image(profileTexture);
        Label nameLabel = new Label(playerName, new Label.LabelStyle(smallFont, skin.getColor("white")));
        nameLabel.setFontScale(2f);

        profileCol.add(profileImage).size(250).row();
        profileCol.add(nameLabel).padTop(40).width(250).center();

        return profileCol;
    }

    private Label createScoreLabel() {
        String scoreText = controller.getPlayerOnePoints() + " - " + controller.getPlayerTwoPoints();
        Label scoreLabel = new Label(scoreText, new Label.LabelStyle(largerFont, skin.getColor("white")));
        scoreLabel.setFontScale(2f);
        return scoreLabel;
    }

    private void initializePlayedRounds() {
        resultTable = new Table();
        if (playedRounds == null) playedRounds = new ArrayList<>();
        else playedRounds.clear();

        for (Object obj : controller.getGames()) {
            RoundModel round = parseRound(obj);
            if (round == null) continue;

            if (controller.isMultiplayer()) {
                // For multiplayer, show rounds where at least one player has answered
                boolean hasP1 = round.getPlayerOneAnswers() != null && !round.getPlayerOneAnswers().isEmpty();
                boolean hasP2 = round.getPlayerTwoAnswers() != null && !round.getPlayerTwoAnswers().isEmpty();
                if (hasP1 || hasP2) playedRounds.add(round);
            } else {
                // For singleplayer, just check if player one has answers
                boolean hasP1 = round.getPlayerOneAnswers() != null && !round.getPlayerOneAnswers().isEmpty();
                if (hasP1) playedRounds.add(round);
            }
        }
        updateHeadline();
    }

    private RoundModel parseRound(Object obj) {
        if (obj instanceof RoundModel) {
            return (RoundModel) obj;
        } else if (obj instanceof Map) {
            return RoundModel.fromMap((Map<String, Object>) obj);
        } else {
            System.err.println("⚠️ Unknown round object type: " + obj.getClass().getSimpleName());
            return null;
        }
    }

    private void addResultScrollBox() {
        ScrollPane scrollPane = new ScrollPane(resultTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        Stack resultBox = new Stack();
        resultBox.add(new Image(new TextureRegionDrawable(textFieldTexture)));
        resultBox.add(scrollPane);
        resultBox.setSize(850f, 1200f);
        resultBox.setPosition((Gdx.graphics.getWidth() - 850f) / 2f, 450f);
        stage.addActor(resultBox);
    }

    private void addNavigationButtons() {
        Table navButtons = new Table();
        navButtons.setFillParent(true);
        navButtons.bottom().padBottom(100);

        TextButton prevBtn = new TextButton("← Previous", skin);
        TextButton nextBtn = new TextButton("Next →", skin);
        prevBtn.getLabel().setFontScale(1.2f);
        nextBtn.getLabel().setFontScale(1.2f);

        prevBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (displayedRoundIndex > 0) {
                    displayedRoundIndex--;
                    showRoundResult(displayedRoundIndex);
                }
            }
        });

        nextBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (displayedRoundIndex < playedRounds.size() - 1) {
                    displayedRoundIndex++;
                    showRoundResult(displayedRoundIndex);
                }
            }
        });

        navButtons.add(prevBtn).padRight(30);
        navButtons.add(nextBtn);
        stage.addActor(navButtons);
    }

    private void addPlayRoundButtonIfAvailable() {
        controller.whoAmI(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                UserModel user = (UserModel) data;
                String myUsername = user.getUsername();

                int currentRoundIndex = controller.getCurrentRoundIndex();
                List<RoundModel> allRounds = controller.getGames();

                System.out.println("Current round index: " + currentRoundIndex);
                System.out.println("Total rounds: " + allRounds.size());

                // Check if we're at the end of the game
                if (currentRoundIndex >= allRounds.size()) {
                    System.out.println("Game is finished!");
                    return;
                }

                RoundModel currentRound = allRounds.get(currentRoundIndex);

                // For multiplayer, check if both players have completed the round
                if (controller.isMultiplayer()) {
                    boolean bothPlayersCompleted = currentRound.hasPlayerCompleted(controller.getPlayerOne()) &&
                                                 currentRound.hasPlayerCompleted(controller.getPlayerTwo());

                    System.out.println("Both players completed: " + bothPlayersCompleted);

                    // If this is the first round or both players have completed the current round
                    if (currentRoundIndex == 0 || bothPlayersCompleted) {
                        // Show play button for the current round
                        Button playRoundButton = createPlayRoundButton(currentRoundIndex);
                        stage.addActor(playRoundButton);
                    }
                } else {
                    // For single player, just check if player one has completed
                    boolean playerOneCompleted = currentRound.hasPlayerCompleted(controller.getPlayerOne());
                    if (currentRoundIndex == 0 || playerOneCompleted) {
                        Button playRoundButton = createPlayRoundButton(currentRoundIndex);
                        stage.addActor(playRoundButton);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("❌ Could not determine current user: " + e.getMessage());
            }
        });
    }

    private Button createPlayRoundButton(int currentRoundIndex) {
        TextureRegionDrawable playRoundDrawable = new TextureRegionDrawable(playRoundTexture);
        Button.ButtonStyle playRoundStyle = new Button.ButtonStyle();
        playRoundStyle.up = playRoundDrawable;

        Button playRoundButton = new Button(playRoundStyle);
        playRoundButton.setSize(850f, 200);
        playRoundButton.setPosition(Gdx.graphics.getWidth() / 2f - playRoundButton.getWidth() / 2f, 200);

        playRoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.setActiveRoundIndex(currentRoundIndex);
                System.out.println("Current round is: " + currentRoundIndex);
                controller.goToRound();
            }
        });

        return playRoundButton;
    }



    private void initializeSingleplayer() {
        commonInitialize();

        String headlineText;
        if (playedRounds == null) playedRounds = new ArrayList<>();
        else playedRounds.clear();

        if ((playedRounds.size()) == controller.getRounds()) {
            headlineText = "Game over! View the leaderboard.";
        } else if (playedRounds.isEmpty()) {
            headlineText = "Press Start Game to play";
        } else {
            headlineText = "Press Next Round to continue";
        }

        headlineLabel = new Label(headlineText, new Label.LabelStyle(largerFont, skin.getColor("white")));
        headlineLabel.setFontScale(1.5f);
        headlineLabel.setPosition(120, Gdx.graphics.getHeight() - 250);
        stage.addActor(headlineLabel);

        Table scoreRow = new Table();
        Table profileCol = new Table();
        Image profile = new Image(profileTexture);
        Label nameLabel = new Label(controller.getPlayerOne(), new Label.LabelStyle(smallFont, skin.getColor("white")));
        nameLabel.setFontScale(2f);
        profileCol.add(profile).size(250).row();
        profileCol.add(nameLabel).padTop(40).width(250).center();
        scoreRow.add(profileCol).padRight(80);
        scoreRow.setPosition(580, Gdx.graphics.getHeight() - 500);
        stage.addActor(scoreRow);

        resultTable = new Table();
        playedRounds.clear();

        for (Object obj : controller.getGames()) {
            RoundModel round;

            if (obj instanceof RoundModel) {
                round = (RoundModel) obj;
            } else if (obj instanceof Map) {
                round = RoundModel.fromMap((Map<String, Object>) obj);
            } else {
                System.err.println("⚠️ Unknown round object type: " + obj.getClass().getSimpleName());
                continue;
            }

            boolean hasP1 = round.getPlayerOneAnswers() != null && !round.getPlayerOneAnswers().isEmpty();
            if (hasP1) {
                playedRounds.add(round);
            }
        }

        updateHeadline();
        ScrollPane scrollPane = new ScrollPane(resultTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        Stack resultBox = new Stack();
        resultBox.add(new Image(new TextureRegionDrawable(textFieldTexture)));
        resultBox.add(scrollPane);
        resultBox.setSize(850f, 1200f);
        resultBox.setPosition((Gdx.graphics.getWidth() - 850f) / 2f, 450f);
        stage.addActor(resultBox);

        Table navButtons = new Table();
        navButtons.setFillParent(true);
        navButtons.bottom().padBottom(100);

        TextButton prevBtn = new TextButton("← Previous", skin);
        TextButton nextBtn = new TextButton("Next →", skin);
        prevBtn.getLabel().setFontScale(1.2f);
        nextBtn.getLabel().setFontScale(1.2f);

        prevBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (displayedRoundIndex > 0) {
                    displayedRoundIndex--;
                    showRoundResult(displayedRoundIndex);
                }
            }
        });

        nextBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (displayedRoundIndex < playedRounds.size() - 1) {
                    displayedRoundIndex++;
                    showRoundResult(displayedRoundIndex);
                }
            }
        });

        navButtons.add(prevBtn).padRight(30);
        navButtons.add(nextBtn);
        stage.addActor(navButtons);

        if (!playedRounds.isEmpty()) {
            showRoundResult(displayedRoundIndex);
        }

        if (playedRounds.size() != controller.getRounds()) {
            TextureRegionDrawable nextRoundUp;
            TextureRegionDrawable nextRoundDisabled;

            if (playedRounds.isEmpty()) {
                nextRoundUp = new TextureRegionDrawable(startGameTexture);
                nextRoundDisabled = new TextureRegionDrawable(startGameDisabledTexture);
            } else {
                nextRoundUp = new TextureRegionDrawable(nextRoundTexture);
                nextRoundDisabled = new TextureRegionDrawable(nextRoundDisabledTexture);
            }

            Button.ButtonStyle nextRoundStyle = new Button.ButtonStyle();
            nextRoundStyle.up = nextRoundUp;
            nextRoundStyle.disabled = nextRoundDisabled;
            Button nextRound = new Button(nextRoundStyle);
            nextRound.setSize(850f, 200);
            nextRound.setPosition(Gdx.graphics.getWidth() / 2 - nextRound.getWidth() / 2, 200);
            nextRound.setDisabled(false);

            nextRound.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!nextRound.isDisabled()) {
                        int currentRoundIndex = playedRounds.size();

                        List<RoundModel> rounds = controller.getGames();
                        if (currentRoundIndex >= rounds.size()) currentRoundIndex = rounds.size() - 1;

                        System.out.println("CurrentRound is: " + currentRoundIndex);

                        controller.setActiveRoundIndex(currentRoundIndex);
                        controller.goToRoundSingleplayer();
                    }
                }
            });
            stage.addActor(nextRound);
        } else {
            TextureRegionDrawable showLeaderBoardUp = new TextureRegionDrawable(showLeaderBoardTexture);
            Button.ButtonStyle showLeaderBoardStyle = new Button.ButtonStyle();
            showLeaderBoardStyle.up = showLeaderBoardUp;
            Button showLeaderBoard = new Button(showLeaderBoardStyle);
            showLeaderBoard.setSize(850f, 200);
            showLeaderBoard.setPosition(Gdx.graphics.getWidth() / 2 - showLeaderBoard.getWidth() / 2, 200);
            showLeaderBoard.setDisabled(false); // No multiplayer check needed

            showLeaderBoard.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    controller.goToLeaderBoard();
                }
            });
            stage.addActor(showLeaderBoard);
        }
    }

    private void commonInitialize(){
        controller.checkForRounds();

        background.setFillParent(true);
        stage.addActor(background);

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

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(40);
        stage.addActor(table);
    }


    private void showRoundResult(int index) {
        if (index < 0 || index >= playedRounds.size()) return;

        resultTable.clear();
        RoundModel round = playedRounds.get(index);
        int roundNumber = index + 1;

        Label.LabelStyle headerStyle = new Label.LabelStyle(roundFont, Color.BLACK);
        Label.LabelStyle answerStyle = new Label.LabelStyle(answerFont, Color.BLACK);

        resultTable.add(new Label("Round " + roundNumber, headerStyle)).padBottom(15).row();

        resultTable.add(new Label(controller.getPlayerOne() + ": " + round.getPlayerOneScore() + " pts", headerStyle)).left().padBottom(5).row();
        if (controller.isMultiplayer()) {
            resultTable.add(new Label(controller.getPlayerTwo() + ": " + round.getPlayerTwoScore() + " pts", headerStyle)).left().padBottom(5).row();
        }

        resultTable.add(new Label("Answers:", headerStyle)).padTop(10).padBottom(10).row();

        for (Map.Entry<String, Integer> entry : round.getPlayerOneAnswers().entrySet()) {
            Table row = new Table();
            Label answer = new Label(entry.getKey(), answerStyle);
            Label pointLabel = new Label((entry.getValue() > 0 ? "+" : "") + entry.getValue(),
                new Label.LabelStyle(answerFont, entry.getValue() > 0 ? Color.GREEN : Color.BLACK));

            row.add(new Label(controller.getPlayerOne() + ": ", answerStyle)).left().padRight(5);
            row.add(answer).left().padRight(15);
            row.add(pointLabel).right();
            resultTable.add(row).left().padBottom(5).row();
        }

        if (controller.isMultiplayer()) {
            for (Map.Entry<String, Integer> entry : round.getPlayerTwoAnswers().entrySet()) {
                Table row = new Table();
                Label answer = new Label(entry.getKey(), answerStyle);
                Label pointLabel = new Label((entry.getValue() > 0 ? "+" : "") + entry.getValue(),
                    new Label.LabelStyle(answerFont, entry.getValue() > 0 ? Color.GREEN : Color.BLACK));

                row.add(new Label(controller.getPlayerTwo() + ": ", answerStyle)).left().padRight(5);
                row.add(answer).left().padRight(15);
                row.add(pointLabel).right();
                resultTable.add(row).left().padBottom(5).row();
            }
        }
    }

    public void updateHeadline() {
        if (headlineLabel == null) return;

        if ((playedRounds.size()) == controller.getRounds()) {
            headlineLabel.setText("Game over! View the leaderboard.");
        } else if (playedRounds.isEmpty()) {
            headlineLabel.setText("Press Start Game to play");
        } else {
            headlineLabel.setText("Press Next Round to continue");
        }
    }


    public void update() {}

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        backButtonTexture.dispose();
        skin.dispose();
        smallFont.dispose();
        largerFont.dispose();
        roundFont.dispose();
        profileTexture.dispose();
        startGameTexture.dispose();
        textFieldTexture.dispose();
        nextRoundTexture.dispose();
        nextRoundDisabledTexture.dispose();
        showLeaderBoardTexture.dispose();
        startGameDisabledTexture.dispose();
    }
}
