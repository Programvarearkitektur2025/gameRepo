package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.progark.Client.Controllers.LobbyController;
import io.github.progark.Client.Model.LobbyModel;
import io.github.progark.Client.Service.LobbyService;
import io.github.progark.Client.Views.Game.LobbyView;
import io.github.progark.Main;

public class CreateGameView implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private LobbyController lobbyController;
    private LobbyModel lobbyModel;

    private Texture backgroundTexture;
    private Image background;

    public CreateGameView(Main game) {
        this.game = game;
        lobbyModel = new LobbyModel();
        LobbyService lobbyService = new LobbyService(game.getDbInstance());
        lobbyController = new LobbyController(lobbyService, lobbyModel, new LobbyView() {});

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton createButton = new TextButton("Create Lobby", skin);
        createButton.getLabel().setFontScale(2f);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lobbyController.createLobby();
            }
        });



        table.add(createButton).width(400).height(150).center();


    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}
