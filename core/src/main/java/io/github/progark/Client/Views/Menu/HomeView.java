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

import io.github.progark.Client.Views.View;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;

import java.util.Arrays;
import java.util.List;

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

    public HomeView(Main game, AuthService authService) {
        super();
        this.game = game;
        this.authService = authService;
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
    }

    @Override
    protected void initialize() {
        background.setFillParent(true);
        stage.addActor(background);

        Table mainLayout = new Table();
        mainLayout.setFillParent(true);
        mainLayout.bottom();
        stage.addActor(mainLayout);

        List<String> yourTurnGames = Arrays.asList("George");
        List<String> theirTurnGames = Arrays.asList("Molly", "Clara");

        mainLayout.top();
        mainLayout.add(yourTurn).left().pad(30);
        mainLayout.row();
        for (String name : yourTurnGames) {
            Table entry = createGameEntry(name);
            mainLayout.add(entry).padLeft(120).width(400);
            mainLayout.row();
        }

        mainLayout.add(theirTurn).left().pad(30);
        mainLayout.row();
        for (String name : theirTurnGames) {
            Table entry = createGameEntry(name);
            mainLayout.add(entry).padLeft(120).width(400);
            mainLayout.row();
        }

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

        buttonTable.add(joinGameButton).pad(20).size(480);
        buttonTable.add(createGameButton).pad(20).size(500);
        mainLayout.add(buttonTable).colspan(2).padTop(20).padBottom(20);
        mainLayout.row();

        Table navWrapper = new Table();
        navWrapper.bottom().add(navBar).expandX().fillX().height(300);
        mainLayout.add(navWrapper).expandY().bottom().fillX().colspan(2);
    }

    private Table createGameEntry(String playerName) {
        Table entry = new Table();

        Image avatar = new Image(avatarTexture);
        Label nameLabel = new Label(playerName, skin);
        nameLabel.setFontScale(2.5f);
        nameLabel.setColor(Color.BLACK);
        nameLabel.setAlignment(Align.left);
        nameLabel.setWrap(false);


        Stack stack = new Stack();
        stack.add(avatar);

        Table labelTable = new Table();
        labelTable.add(nameLabel).left().top();
        stack.add(labelTable);

        entry.add(stack).size(1100, 250).pad(5);
        return entry;
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
