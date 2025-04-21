package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import java.util.List;
import java.util.Map;

import io.github.progark.Client.Controllers.HomeController;
import io.github.progark.Client.Views.View;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.HomeModel;
import io.github.progark.Client.Views.Components.NavBar;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.database.DataCallback;

public class HomeView extends View {

    private final Skin skin;
    private Texture backgroundTexture, yourTurnTexture, theirTurnTexture, joinGameTexture,
        createGameTexture, avatarTexture, navBarTexture, howToButtonTexture, transparentTexture;

    private final HomeController controller;
    private Table contentTable;
    private Image background;
    private NavBar navBar;

    public HomeView(HomeController controller) {
        super();
        this.controller = controller;

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        enter();
    }

    @Override
    protected void initialize() {
        this.backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        yourTurnTexture = new Texture(Gdx.files.internal("yourTurn.png"));
        theirTurnTexture = new Texture(Gdx.files.internal("theirTurn.png"));
        joinGameTexture = new Texture(Gdx.files.internal("joinGame2.png"));
        createGameTexture = new Texture(Gdx.files.internal("newGame2.png"));
        avatarTexture = new Texture(Gdx.files.internal("gameAvatar.png"));
        navBarTexture = new Texture(Gdx.files.internal("navBar2.png"));
        howToButtonTexture = new Texture(Gdx.files.internal("HowToButton.png"));
        transparentTexture = new Texture(Gdx.files.internal("Transparent.png"));

        background = new Image(backgroundTexture);
        stage.addActor(background);

        // How To Play button
        ImageButton howToButton = new ImageButton(new TextureRegionDrawable(howToButtonTexture));
        howToButton.setSize(200, 200);
        howToButton.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 200);
        howToButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.openTutorialPage();
            }
        });


        Table mainLayout = new Table();
        mainLayout.setFillParent(true);
        mainLayout.top().padTop(30);
        stage.addActor(mainLayout);



        // Game buttons
        Table buttonTable = new Table();
        ImageButton joinGameButton = new ImageButton(new TextureRegionDrawable(joinGameTexture));
        ImageButton createGameButton = new ImageButton(new TextureRegionDrawable(createGameTexture));

        joinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.ViewJoinGamePage();
            }
        });

        createGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.ViewCreateGamePage();
            }
        });

        buttonTable.add(joinGameButton).pad(20).size(480);
        buttonTable.add(createGameButton).pad(20).size(500);
        mainLayout.add(buttonTable).colspan(2).padBottom(20).row();

        // Content area
        contentTable = new Table();
        contentTable.top().left().padTop(20);

        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        mainLayout.add(scrollPane).colspan(2).expand().fill().row();


        // Navigation bar
        navBar = new NavBar(stage, controller.getMain());
        stage.addActor(howToButton);


        controller.getMain().getMusicManager().setVolume(1.0f);

        // Fetch and render games
        controller.getRelevantGames(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                List<Map<String, Object>> rawLobbies = (List<Map<String, Object>>) data;
                renderGames(rawLobbies);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to display relevant games: " + e.getMessage());
            }
        });
    }



    private Stack createGameEntry(HomeModel.GameEntry game) {
        float avatarSize = Gdx.graphics.getWidth() * 1f;

        Image avatar = new Image(new TextureRegionDrawable(new TextureRegion(avatarTexture)));
        avatar.setScaling(Scaling.fit);

        String opponent = game.getOpponentName() != null ? game.getOpponentName() : "Waiting for Player to join";
        Label nameLabel = new Label(opponent, skin);
        nameLabel.setFontScale(3f);
        nameLabel.setColor(0, 0, 0, 1);
        nameLabel.setAlignment(Align.center);

        Stack stack = new Stack();
        stack.setSize(avatarSize, avatarSize);
        stack.add(avatar);
        stack.add(nameLabel);

        // ONLY the image+text combo is clickable now
        stack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.getGameToOpen(game.getGameId());
            }
        });

        return stack;
    }

    public void updateGameLists(HomeModel homeModel) {
        contentTable.clear();
        float sectionWidth = Gdx.graphics.getWidth() * 1f;
        float avatarAspectRatio = 2703f / 551f;
        float avatarHeight = sectionWidth / avatarAspectRatio;

        // "Your turn" section header
        Image yourTurnHeader = new Image(yourTurnTexture);
        contentTable.add(yourTurnHeader).left().padTop(30).padBottom(20).row();

        for (HomeModel.GameEntry game : homeModel.getYourTurnGames()) {
            Stack entry = createGameEntry(game);
            contentTable.add(entry)
                .width(sectionWidth)
                .height(avatarHeight)
                .center()
                .padBottom(30)
                .row();
        }

        // "Their turn" section header
        Image theirTurnHeader = new Image(theirTurnTexture);
        contentTable.add(theirTurnHeader).left().padTop(30).padBottom(20).row();

        for (HomeModel.GameEntry game : homeModel.getTheirTurnGames()) {
            Stack entry = createGameEntry(game);
            contentTable.add(entry)
                .width(sectionWidth)
                .height(avatarHeight)
                .center()
                .padBottom(30)
                .row();
        }
    }



    public void renderGames(List<Map<String, Object>> rawGameList) {
        controller.getLoggedInUserHome(new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                String currentUser = (String) result;
                HomeModel homeModel = new HomeModel();

                for (Map<String, Object> entry : rawGameList) {
                    String gameId = (String) entry.get("gameId");
                    GameModel game = (GameModel) entry.get("game");

                    if (game == null) continue;

                    String displayName;
                    String opponent = game.getOpponent(currentUser);
                    List<RoundModel> rounds = game.getGames();
                    int totalRounds = game.getRounds();
                    int playedRounds = 0;

                    for (RoundModel round : rounds) {
                        if (round.getPlayerOneAnswers().size() > 0 || round.getPlayerTwoAnswers().size() > 0) {
                            playedRounds++;
                        }
                    }

                    // ✅ SKIP if game is finished
                    if (!game.isMultiplayer()) {
                        if (playedRounds >= totalRounds) continue;
                    } else {
                        // Only skip if both players are done with all rounds
                        int completedByBoth = 0;
                        for (RoundModel round : rounds) {
                            if (round.hasBothPlayersAnswered()) {
                                completedByBoth++;
                            }
                        }
                        if (completedByBoth >= totalRounds) continue;
                    }

                    // ➤ SINGLEPLAYER DISPLAY
                    if (!game.isMultiplayer()) {
                        displayName = "You";
                        homeModel.getYourTurnGames().add(new HomeModel.GameEntry(gameId, displayName, game.getStatus()));
                        continue;
                    }

                    // ➤ WAITING FOR OPPONENT
                    if (opponent == null || opponent.isEmpty()) {
                        displayName = "Waiting for Player to join";
                        homeModel.getTheirTurnGames().add(new HomeModel.GameEntry(gameId, displayName, game.getStatus()));
                        continue;
                    }

                    // ➤ MULTIPLAYER LOGIC
                    displayName = opponent;
                    int currentRound = game.getCurrentRound().intValue();
                    boolean hasPlayed = false;

                    if (currentRound < rounds.size()) {
                        RoundModel current = rounds.get(currentRound);
                        if (current != null) {
                            hasPlayed = current.hasPlayerCompleted(currentUser);
                        }
                    }

                    if (!hasPlayed) {
                        homeModel.getYourTurnGames().add(new HomeModel.GameEntry(gameId, displayName, game.getStatus()));
                    } else {
                        homeModel.getTheirTurnGames().add(new HomeModel.GameEntry(gameId, displayName, game.getStatus()));
                    }
                }

                updateGameLists(homeModel);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to fetch current user: " + e.getMessage());
            }
        });
    }




    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        yourTurnTexture.dispose();
        theirTurnTexture.dispose();
        joinGameTexture.dispose();
        createGameTexture.dispose();
        avatarTexture.dispose();
        navBarTexture.dispose();
        howToButtonTexture.dispose();
        transparentTexture.dispose();
        if (navBar != null) navBar.dispose();
    }
}
