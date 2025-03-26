package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private final Main game;

    public HomeView(Main game, AuthService authService) {
        super();
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        yourTurnTexture = new Texture(Gdx.files.internal("yourTurn.png"));
        theirTurnTexture = new Texture(Gdx.files.internal("theirTurn.png"));
        joinGameTexture = new Texture(Gdx.files.internal("joinGame.png"));
        createGameTexture = new Texture(Gdx.files.internal("createGame.png"));
    }

    @Override
    protected void initialize() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        //List<String> yourTurnGames = Arrays.asList("George");
        //List<String> theirTurnGames = Arrays.asList("Molly", "Clara");

        Image yourTurnImage = new Image(yourTurnTexture);
        root.add(yourTurnImage).left().pad(10);
        root.row();
        /**for (String name : yourTurnGames) {
            TextButton button = new TextButton(name + "  >", skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Handle game open
                }
            });
            root.add(button).width(300).pad(5);
            root.row();
        }**/

        Image theirTurnImage = new Image(theirTurnTexture);
        root.add(theirTurnImage).left().pad(10);
        root.row();
        /**for (String name : theirTurnGames) {
            TextButton button = new TextButton(name + "  >", skin);
            button.setTouchable(Touchable.disabled);
            root.add(button).width(300).pad(5);
            root.row();
        }*/

        Table buttonTable = new Table();

        ImageButton joinGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(joinGameTexture)));
        joinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getViewManager().setView(() -> new JoinGameView(game));
            }
        });

        ImageButton createGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(createGameTexture)));
        createGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getViewManager().setView(() -> new CreateGameView(game));
            }
        });

        buttonTable.add(joinGameButton).pad(10);
        buttonTable.add(createGameButton).pad(10);
        root.add(buttonTable).colspan(2);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        yourTurnTexture.dispose();
        theirTurnTexture.dispose();
        joinGameTexture.dispose();
        createGameTexture.dispose();
    }
}
