package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Arrays;
import java.util.List;

import io.github.progark.Client.Controllers.HomeController;
import io.github.progark.Client.Views.View;
import io.github.progark.Server.Model.Game.HomeModel;
import io.github.progark.Client.Views.Components.NavBar;

public class HomeView extends View {

    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture yourTurnTexture;
    private final Texture theirTurnTexture;
    private final Texture joinGameTexture;
    private final Texture createGameTexture;
    private final Texture avatarTexture;
    private final Texture navBarTexture;
    private final Texture howToButtonTexture;
    private final Texture transparentTexture;
    private NavBar navBar;

    private Image background;
    private final HomeController controller;
    private Table contentTable;

    public HomeView(HomeController controller) {
        super();
        this.navBar = navBar;
        this.controller = controller;

        // Load assets
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

        enter();
    }

    @Override
    protected void initialize() {
        background.setFillParent(true);
        stage.addActor(background);

        // How To button (top-right corner)
        ImageButton howToButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(howToButtonTexture)));
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
        mainLayout.bottom();
        stage.addActor(mainLayout);

        List<String> yourTurnGames = Arrays.asList("George");
        List<String> theirTurnGames = Arrays.asList("Molly", "Clara");

        mainLayout.top();
        mainLayout.add(new Image(yourTurnTexture)).left().pad(30);
        mainLayout.row();

        // Game Buttons
        Table buttonTable = new Table();
        ImageButton joinGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(joinGameTexture)));
        ImageButton createGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(createGameTexture)));

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
        mainLayout.add(buttonTable).colspan(2).padTop(20).padBottom(20);
        mainLayout.row();

        navBar = new NavBar(stage, controller.getMain());
        /*
        if (!controller.getMain().getMusicManager().isPlaying()) {
            controller.getMain().getMusicManager().play("ThinkingOutLoud.mp3");
        }
         */
        controller.getMain().getMusicManager().setVolume(1.0f);




    }

    public void updateGameLists(HomeModel homeModel) {
        contentTable.clear();

        contentTable.add(new Image(yourTurnTexture)).size(200, 60).left().pad(30).row();
        for (HomeModel.GameEntry game : homeModel.getYourTurnGames()) {
            Table entry = createGameEntry(game);
            entry.setTouchable(Touchable.enabled);
            entry.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("ClickedGameEntry");
                }
            });
            contentTable.add(entry).width(Gdx.graphics.getWidth() * 0.8f).height(100).padLeft(120).row();
        }

        contentTable.add(new Image(theirTurnTexture)).size(200, 60).left().pad(30).row();
        for (HomeModel.GameEntry game : homeModel.getTheirTurnGames()) {
            Table entry = createGameEntry(game);
            entry.setTouchable(Touchable.enabled);
            entry.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("ClickedGameEntry");
                }
            });
            contentTable.add(entry).width(Gdx.graphics.getWidth() * 0.8f).height(100).padLeft(120).row();
        }

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
        return new Table(); // Placeholder
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
        navBar.dispose();
    }
}
