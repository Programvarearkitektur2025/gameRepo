package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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


import io.github.progark.Client.Controllers.HomeController;
import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.HomeModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.HomeService;
import io.github.progark.Server.database.DatabaseManager;

public class HomeView extends View {
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture yourTurnTexture;
    private final Texture theirTurnTexture;
    private final Texture joinGameTexture;
    private final Texture createGameTexture;
    private final Texture avatarTexture;
    private final Texture navBarTexture;

    private Image background;
    private Image yourTurn;
    private Image theirTurn;
    private Image joinGame;
    private Image createGame;
    private Image navBar;
    private final Main game;
    private final AuthService authService;
    private final HomeController homeController;
    private Table contentTable;

    public HomeView(Main game, AuthService authService) {
        super();
        this.game = game;
        this.authService = authService;

        // Initialize MVC components
        HomeModel homeModel = new HomeModel();
        DatabaseManager databaseManager = game.getDatabaseManager();
        HomeService homeService = new HomeService(databaseManager);
        this.homeController = new HomeController(homeService, homeModel);

        // Initialize UI components
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        yourTurnTexture = new Texture(Gdx.files.internal("yourTurn.png"));
        theirTurnTexture = new Texture(Gdx.files.internal("theirTurn.png"));
        joinGameTexture = new Texture(Gdx.files.internal("joinGame2.png"));
        createGameTexture = new Texture(Gdx.files.internal("newGame2.png"));
        avatarTexture = new Texture(Gdx.files.internal("gameAvatar.png"));
        navBarTexture = new Texture(Gdx.files.internal("navBar2.png"));

        background = new Image(backgroundTexture);
        yourTurn = new Image(yourTurnTexture);
        theirTurn = new Image(theirTurnTexture);
        joinGame = new Image(joinGameTexture);
        createGame = new Image(createGameTexture);
        navBar = new Image(navBarTexture);

        // Set up controller-view connection
        homeController.setHomeView(this);
    }

    @Override
    protected void initialize() {
        // Set up background
        background.setFillParent(true);
        stage.addActor(background);

        // Create main container
        Table mainContainer = new Table();
        mainContainer.setFillParent(true);
        stage.addActor(mainContainer);

        // Create content table for game lists and buttons
        contentTable = new Table();
        contentTable.setFillParent(true);
        mainContainer.add(contentTable).expand().fill();

        // Load user's games
        String userId = authService.getCurrentUserEmail();
        homeController.loadUserGames(userId);

        // Button section
        Table buttonTable = new Table();
        ImageButton joinGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(joinGameTexture)));
        ImageButton createGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(createGameTexture)));

        joinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getViewManager().setView(() -> new JoinGameView(game));
            }
        });

        createGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getViewManager().setView(() -> new CreateGameView(game));
            }
        });

        float buttonWidth = Gdx.graphics.getWidth() * 0.4f;
        float buttonHeight = buttonWidth * 0.3f;

        buttonTable.add(joinGameButton).size(buttonWidth, buttonHeight).pad(20);
        buttonTable.add(createGameButton).size(buttonWidth, buttonHeight).pad(20);
        contentTable.add(buttonTable).colspan(2).padTop(20).padBottom(20).row();

        // Navigation bar
        Table navWrapper = new Table();
        navWrapper.setFillParent(true);
        navWrapper.bottom();
        navWrapper.add(navBar).expandX().fillX().height(Gdx.graphics.getHeight() * 0.15f);
        mainContainer.add(navWrapper).expandX().fillX().bottom();
    }

    public void updateGameLists(HomeModel homeModel) {
        // Clear existing game lists
        contentTable.clear();

        // Add your turn section
        contentTable.add(yourTurn).size(200, 60).left().pad(30).row();
        for (HomeModel.GameEntry game : homeModel.getYourTurnGames()) {
            Table entry = createGameEntry(game);
            entry.setTouchable(Touchable.enabled);
            entry.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    homeController.handleGameEntryClick(game.getGameId());
                }
            });
            contentTable.add(entry).width(Gdx.graphics.getWidth() * 0.8f).height(100).padLeft(120).row();
        }

        // Add their turn section
        contentTable.add(theirTurn).size(200, 60).left().pad(30).row();
        for (HomeModel.GameEntry game : homeModel.getTheirTurnGames()) {
            Table entry = createGameEntry(game);
            entry.setTouchable(Touchable.enabled);
            entry.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    homeController.handleGameEntryClick(game.getGameId());
                }
            });
            contentTable.add(entry).width(Gdx.graphics.getWidth() * 0.8f).height(100).padLeft(120).row();
        }

        // Re-add buttons
        Table buttonTable = new Table();
        ImageButton joinGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(joinGameTexture)));
        ImageButton createGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(createGameTexture)));

        float buttonWidth = Gdx.graphics.getWidth() * 0.4f;
        float buttonHeight = buttonWidth * 0.3f;

        buttonTable.add(joinGameButton).size(buttonWidth, buttonHeight).pad(20);
        buttonTable.add(createGameButton).size(buttonWidth, buttonHeight).pad(20);
        contentTable.add(buttonTable).colspan(2).padTop(20).padBottom(20).row();
    }

    private Table createGameEntry(HomeModel.GameEntry game) {
        Table entry = new Table();
        entry.setBackground(skin.getDrawable("default-pane"));

        Image avatar = new Image(avatarTexture);
        avatar.setScaling(Scaling.fit);
        Label nameLabel = new Label(game.getOpponentName(), skin);
        nameLabel.setFontScale(1.5f);
        nameLabel.setColor(Color.BLACK);
        nameLabel.setAlignment(Align.left);
        nameLabel.setWrap(false);

        Stack stack = new Stack();
        stack.add(avatar);

        Table labelTable = new Table();
        labelTable.add(nameLabel).left().top();
        stack.add(labelTable);

        entry.add(stack).size(80, 80).pad(5);
        return entry;
    }

    public void navigateToGame(String gameId) {
        game.getViewManager().setView(() -> new GameView(game));
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
    }
}
