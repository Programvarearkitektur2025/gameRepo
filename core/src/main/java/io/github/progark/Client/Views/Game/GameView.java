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
    private final Texture backgroundTexture, nextRoundTexture, nextRoundDisabledTexture, playRoundTexture, showLeaderBoardTexture, profileTexture, startGameTexture, textFieldTexture, backButtonTexture, startGameDisabledTexture, prevBtnTexture, nextBtnTexture;
    private final BitmapFont smallFont, largerFont, answerFont;
    private final GameController controller;
    private Image background;
    private int displayedRoundIndex = 0;
    private Table resultTable;
    private List<RoundModel> playedRounds;
    private boolean initialized = false;

    private boolean leaderboardButtonAdded = false;


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
        nextBtnTexture = new Texture(Gdx.files.internal("next-round-button3.png"));
        prevBtnTexture = new Texture(Gdx.files.internal("previous-round-button3.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        this.smallFont = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 60;
        this.largerFont = generator.generateFont(parameter2);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter4 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter4.size = 35;
        this.answerFont = generator.generateFont(parameter4);

        generator.dispose();

        background = new Image(backgroundTexture);

    }

    @Override
    protected void initialize() {
        if (initialized) return;

        if (controller.isMultiplayer()){
            initializeMultiplayer();
        } else {
            initializeSingleplayer();
        }
        initialized = true;
    }

    private void initializeMultiplayer() {
        commonInitialize();
        addLobbyCodeLabel();
        addPlayerScoreRow();
        initializePlayedRounds();
        addResultScrollBox();
        addNavigationButtons();
        if (!playedRounds.isEmpty()) showRoundResult(displayedRoundIndex);
        addPlayRoundButtonIfAvailable(); // this is now guarded inside
        if (playedRounds.size() == controller.getRounds() && !leaderboardButtonAdded) {
            addShowLeaderboardButton();
            leaderboardButtonAdded = true;
        }

    }


    // PROFILES AND SCORE
    private void addPlayerScoreRow() {
        Table scoreRow = new Table();
        if (controller.isMultiplayer()) {
            scoreRow.add(createPlayerProfile(controller.getPlayerOne())).padRight(60);
            scoreRow.add(createScoreLabel()).padRight(60).center();
            scoreRow.add(createPlayerProfile(controller.getPlayerTwo()));
            scoreRow.setPosition(520, Gdx.graphics.getHeight() - 400);
        } else {
            scoreRow.add(createPlayerProfile(controller.getPlayerOne())).padRight(120);
            scoreRow.setPosition(580, Gdx.graphics.getHeight() - 400);
        }
        stage.addActor(scoreRow);
    }

    private void addLobbyCodeLabel() {
        String lobbyCode = controller.getLobbyCode();
        Label.LabelStyle style = new Label.LabelStyle(largerFont, skin.getColor("white"));
        Label lobbyLabel = new Label("Lobby Code: " + lobbyCode, style);

        lobbyLabel.setPosition(
            (Gdx.graphics.getWidth() - lobbyLabel.getWidth()) / 2f,
            Gdx.graphics.getHeight() - 150
        );
        stage.addActor(lobbyLabel);
    }


    // PROFILE PIC AND USERNAME
    private Table createPlayerProfile(String playerName) {
        Table profileCol = new Table();
        Image profileImage = new Image(profileTexture);
        Label nameLabel = new Label(playerName, new Label.LabelStyle(smallFont, skin.getColor("white")));

        profileCol.add(profileImage).size(250).row();
        profileCol.add(nameLabel).padTop(40).width(250).center();

        return profileCol;
    }

    private Label createScoreLabel() {
        String scoreText = controller.getPlayerOnePoints() + " - " + controller.getPlayerTwoPoints();
        return new Label(scoreText, new Label.LabelStyle(largerFont, skin.getColor("white")));
    }

    private void initializePlayedRounds() {
        resultTable = new Table();
        playedRounds = new ArrayList<>();

        controller.whoAmI(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String username = ((UserModel) data).getUsername();

                for (Object obj : controller.getGames()) {
                    RoundModel round = parseRound(obj);
                    if (round == null) continue;


                    if (round.hasBothPlayersAnswered()) {
                        playedRounds.add(round);
                    }
                }

                if (!playedRounds.isEmpty()) {
                    displayedRoundIndex = 0;
                    showRoundResult(displayedRoundIndex);
                }

                // 🔥 Important: Move leaderboard check here (if multiplayer)
                if (controller.isMultiplayer() && playedRounds.size() == controller.getRounds() && !leaderboardButtonAdded) {
                    addShowLeaderboardButton();
                    leaderboardButtonAdded = true;
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("❌ Could not fetch current user for round filtering.");
            }
        });
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
        if (resultTable == null) {
            resultTable = new Table();
        }

        if (resultTable.getChildren().size == 0) {
            resultTable.add(new Label("No rounds played yet.", new Label.LabelStyle(smallFont, Color.GRAY))).pad(30);
        }

        ScrollPane scrollPane = new ScrollPane(resultTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        Stack resultBox = new Stack();
        resultBox.add(new Image(new TextureRegionDrawable(textFieldTexture)));
        resultBox.add(scrollPane);
        resultBox.setSize(850f, 1200f);
        resultBox.setPosition((Gdx.graphics.getWidth() - 850f) / 2f, 550f);
        stage.addActor(resultBox);
    }


    private void addNavigationButtons() {
        Table navButtons = new Table();
        navButtons.setFillParent(true);
        navButtons.bottom().padBottom(35);

        ImageButton prevBtn = new ImageButton(new TextureRegionDrawable(prevBtnTexture));
        ImageButton nextBtn = new ImageButton(new TextureRegionDrawable(nextBtnTexture));

        prevBtn.getImage().setSize(400f, 200f);
        nextBtn.getImage().setSize(400f, 200f);

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

        navButtons.add(prevBtn).padRight(10).size(420);
        navButtons.add(nextBtn).size(420);

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

                // 👇 NEW: Check that both players exist
                String playerOne = controller.getPlayerOne();
                String playerTwo = controller.getPlayerTwo();
                boolean bothPlayersPresent = playerOne != null && playerTwo != null
                    && !playerOne.isEmpty() && !playerTwo.isEmpty();

                if (!bothPlayersPresent) {
                    System.out.println("Waiting for a second player to join the game.");
                    return; // Don't show button
                }

                if (currentRoundIndex < allRounds.size()) {
                    RoundModel currentRound = allRounds.get(currentRoundIndex);
                    boolean haveIAnswered = currentRound.hasPlayerCompleted(myUsername);
                    if (!haveIAnswered) {
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
        playRoundButton.setPosition(Gdx.graphics.getWidth() / 2 - playRoundButton.getWidth() / 2, 320);

        playRoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.setActiveRoundIndex(currentRoundIndex);
                controller.goToRound();
            }
        });

        return playRoundButton;
    }


    private void initializeSingleplayer() {
        commonInitialize();

        if (playedRounds == null) {
            playedRounds = new ArrayList<>();
        } else {
            playedRounds.clear();
        }


        // USERNAME AND PROFILE PIC
        addPlayerScoreRow();


        // RESULT TABLE AND ANSWERS
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


        // SCROLL PANE
        addResultScrollBox();


        // NAV BUTTONS
        addNavigationButtons();


        // ROUND RESULTS
        if (!playedRounds.isEmpty()) {
            showRoundResult(displayedRoundIndex);
        }

        if (playedRounds.size() != controller.getRounds()) {
            TextureRegionDrawable nextRoundEnabled;
            TextureRegionDrawable nextRoundDisabled;

            if (playedRounds.isEmpty()) {
                nextRoundEnabled = new TextureRegionDrawable(startGameTexture);
                nextRoundDisabled = new TextureRegionDrawable(startGameDisabledTexture);
            } else {
                nextRoundEnabled = new TextureRegionDrawable(nextRoundTexture);
                nextRoundDisabled = new TextureRegionDrawable(nextRoundDisabledTexture);
            }

            Button.ButtonStyle nextRoundStyle = new Button.ButtonStyle();
            nextRoundStyle.up = nextRoundEnabled;
            nextRoundStyle.disabled = nextRoundDisabled;

            Button nextRound = new Button(nextRoundStyle);
            nextRound.setSize(850f, 200);
            nextRound.setPosition(Gdx.graphics.getWidth() / 2 - nextRound.getWidth() / 2, 320);
            nextRound.setDisabled(false);

            nextRound.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!nextRound.isDisabled()) {
                        int currentRoundIndex = playedRounds.size();

                        List<RoundModel> rounds = controller.getGames();
                        if (currentRoundIndex >= rounds.size()) currentRoundIndex = rounds.size() - 1;

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
            showLeaderBoard.setPosition(Gdx.graphics.getWidth() / 2 - showLeaderBoard.getWidth() / 2, 320);
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

        // Label styles
        Label.LabelStyle headerStyle = new Label.LabelStyle(smallFont, Color.BLACK);
        Label.LabelStyle answerStyle = new Label.LabelStyle(answerFont, Color.BLACK);

        // Create separate tables for Player 1 and Player 2
        Table playerOneTable = new Table();
        Table playerTwoTable = new Table();

        playerOneTable.align(Align.left);
        playerTwoTable.align(Align.left);

        // Create a table for displaying the round number and player scores
        resultTable.add(new Label("ROUND " + roundNumber, headerStyle)).colspan(2).padBottom(40).row();

        // Add the answers for Player 1 to its table
        for (Map.Entry<String, Integer> entry : round.getPlayerOneAnswers().entrySet()) {
            Table row = new Table();
            Label answer = new Label(entry.getKey(), answerStyle);
            Label pointLabel = new Label((entry.getValue() > 0 ? "+" : "") + entry.getValue(),
                new Label.LabelStyle(answerFont, entry.getValue() > 0 ? Color.GREEN : Color.BLACK));

            row.add(answer).left().padRight(15);
            row.add(pointLabel).right();
            playerOneTable.add(row).left().padBottom(5).row();
        }

        playerOneTable.add(new Label(round.getPlayerOneScore() + " pts", headerStyle)).padBottom(10).padTop(40).row();


        // Add Player 2 score if multiplayer
        if (controller.isMultiplayer()) {

            // Add the answers for Player 2 to its table
            for (Map.Entry<String, Integer> entry : round.getPlayerTwoAnswers().entrySet()) {
                Table row = new Table();
                Label answer = new Label(entry.getKey(), answerStyle);
                Label pointLabel = new Label((entry.getValue() > 0 ? "+" : "") + entry.getValue(),
                    new Label.LabelStyle(answerFont, entry.getValue() > 0 ? Color.GREEN : Color.BLACK));

                row.add(answer).left().padRight(15);
                row.add(pointLabel).right();
                playerTwoTable.add(row).left().padBottom(5).row();
            }

            playerTwoTable.add(new Label(round.getPlayerTwoScore() + " pts", headerStyle)).padBottom(10).padTop(40).row();
        }

        // Create a parent table to hold both Player 1 and Player 2 tables side by side
        Table sideBySideTable = new Table();
        sideBySideTable.setWidth(1000);
        sideBySideTable.setHeight(2000);
        sideBySideTable.add(playerOneTable).padRight(500);  // Add Player 1 table with some padding on the right
        sideBySideTable.add(playerTwoTable);  // Add Player 2 table

        // Add the sideBySideTable to the resultTable
        resultTable.add(sideBySideTable).padTop(10).row();


        // Scroll pane setup
        ScrollPane scrollPane = new ScrollPane(resultTable);
        scrollPane.setScrollingDisabled(false, true);  // Allow vertical scrolling
        scrollPane.setFadeScrollBars(false);  // Disable scroll bar fading

        // Set the scroll pane size and position
        Stack resultBox = new Stack();
        resultBox.add(new Image(new TextureRegionDrawable(textFieldTexture)));
        resultBox.add(scrollPane);
        resultBox.setSize(850f, 1200f);
        resultBox.setPosition((Gdx.graphics.getWidth() - 850f) / 2f, 550f);  // Adjust position to be centered
        stage.addActor(resultBox);
    }


    public void onRoundsUpdated() {
        playedRounds = new ArrayList<>();

        controller.whoAmI(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String username = ((UserModel) data).getUsername();

                for (Object obj : controller.getGames()) {
                    RoundModel round = parseRound(obj);
                    if (round == null) continue;


                    if (round.hasBothPlayersAnswered()) {
                        playedRounds.add(round);
                    }
                }

                if (!playedRounds.isEmpty() && displayedRoundIndex < playedRounds.size()) {
                    showRoundResult(displayedRoundIndex);
                }

                if (controller.isMultiplayer()) {
                    addPlayRoundButtonIfAvailable();

                    if (playedRounds.size() == controller.getRounds() && !leaderboardButtonAdded) {
                        addShowLeaderboardButton();
                        leaderboardButtonAdded = true;
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("❌ Could not fetch current user to filter updated rounds.");
            }
        });
    }


    private void addShowLeaderboardButton() {
        TextureRegionDrawable showLeaderBoardUp = new TextureRegionDrawable(showLeaderBoardTexture);
        Button.ButtonStyle showLeaderBoardStyle = new Button.ButtonStyle();
        showLeaderBoardStyle.up = showLeaderBoardUp;

        Button showLeaderBoard = new Button(showLeaderBoardStyle);
        showLeaderBoard.setSize(850f, 200);
        showLeaderBoard.setPosition(Gdx.graphics.getWidth() / 2 - showLeaderBoard.getWidth() / 2, 320);
        showLeaderBoard.setDisabled(false);

        showLeaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goToLeaderBoard();
            }
        });

        stage.addActor(showLeaderBoard);
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
        profileTexture.dispose();
        startGameTexture.dispose();
        textFieldTexture.dispose();
        nextRoundTexture.dispose();
        nextRoundDisabledTexture.dispose();
        showLeaderBoardTexture.dispose();
        startGameDisabledTexture.dispose();
    }
}
