package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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

    private Skin skin;
    private Texture backgroundTexture, yourTurnTexture, theirTurnTexture, joinGameTexture,
        createGameTexture, avatarTexture, navBarTexture, howToButtonTexture, transparentTexture;

    private final HomeController controller;
    private Table contentTable;
    private Image background;
    private NavBar navBar;

    public HomeView(HomeController controller) {
        super();
        this.controller = controller;
    }

    @Override
    protected void initialize() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
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
        stage.addActor(howToButton);

        Table mainLayout = new Table();
        mainLayout.setFillParent(true);
        mainLayout.top().padTop(30);
        stage.addActor(mainLayout);

        mainLayout.add(new Image(yourTurnTexture)).left().padBottom(20).row();

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
        mainLayout.add(contentTable).colspan(2).expand().fill().row();

        // Navigation bar
        navBar = new NavBar(stage, controller.getMain());

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

    private Table createGameEntry(HomeModel.GameEntry game) {
        Table row = new Table(skin);
        row.setBackground(skin.newDrawable("white"));

        float avatarSize = Gdx.graphics.getWidth() * 0.12f;
        Image avatar = new Image(new TextureRegionDrawable(new TextureRegion(avatarTexture)));
        avatar.setSize(avatarSize, avatarSize);

        String opponent = game.getOpponentName() != null ? game.getOpponentName() : "Waiting for Player to join";
        Label nameLabel = new Label(opponent, skin);
        nameLabel.setFontScale(1.5f);
        nameLabel.setColor(0, 0, 0, 1);

        row.add(avatar).size(avatarSize).pad(20);
        row.add(nameLabel).left().expandX();

        return row;
    }

    public void updateGameLists(HomeModel homeModel) {
        contentTable.clear();
        float sectionWidth = Gdx.graphics.getWidth() * 0.9f;
        float rowHeight = 120f;

        Label yourTurnLabel = new Label("Your turn", skin);
        yourTurnLabel.setFontScale(1.5f);
        yourTurnLabel.setColor(0.4f, 0.6f, 0.2f, 1);
        contentTable.add(yourTurnLabel).left().pad(20).row();

        for (HomeModel.GameEntry game : homeModel.getYourTurnGames()) {
            Table entry = createGameEntry(game);
            entry.setTouchable(Touchable.enabled);
            entry.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    controller.getGameToOpen(game.getGameId());
                }
            });
            contentTable.add(entry).width(sectionWidth).height(rowHeight).padBottom(10).row();
        }

        Label theirTurnLabel = new Label("Their turn", skin);
        theirTurnLabel.setFontScale(1.5f);
        theirTurnLabel.setColor(1.0f, 0.6f, 0.2f, 1);
        contentTable.add(theirTurnLabel).left().padTop(40).padBottom(10).row();

        for (HomeModel.GameEntry game : homeModel.getTheirTurnGames()) {
            Table entry = createGameEntry(game);
            entry.setTouchable(Touchable.enabled);
            entry.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    controller.getGameToOpen(game.getGameId());
                }
            });
            contentTable.add(entry).width(sectionWidth).height(rowHeight).padBottom(10).row();
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

                    String opponent = game.getOpponent(currentUser);
                    String status = game.getStatus();
                    HomeModel.GameEntry gameEntry = new HomeModel.GameEntry(gameId, opponent, status);

                    int currentRound = game.getCurrentRound().intValue() - 1;
                    boolean hasPlayed = false;

                    if (currentRound >= 0 && currentRound < game.getGames().size()) {
                        Object roundObj = game.getGames().get(currentRound);
                        if (roundObj instanceof Map) {
                            RoundModel round = RoundModel.fromMap((Map<String, Object>) roundObj);
                            hasPlayed = round.hasPlayerCompleted(currentUser);
                        }
                    }

                    if (!hasPlayed) {
                        homeModel.getYourTurnGames().add(gameEntry);
                    } else {
                        homeModel.getTheirTurnGames().add(gameEntry);
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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (yourTurnTexture != null) yourTurnTexture.dispose();
        if (theirTurnTexture != null) theirTurnTexture.dispose();
        if (joinGameTexture != null) joinGameTexture.dispose();
        if (createGameTexture != null) createGameTexture.dispose();
        if (avatarTexture != null) avatarTexture.dispose();
        if (navBarTexture != null) navBarTexture.dispose();
        if (howToButtonTexture != null) howToButtonTexture.dispose();
        if (transparentTexture != null) transparentTexture.dispose();
        if (navBar != null) navBar.dispose();
    }
}
