package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.GL20;


import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;


import io.github.progark.Main;
public class HomeView implements Screen {
    private final Main game;
    private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Image yourTurn;
    private final Image theirTurn;

    public HomeView(Main game) {
        this.game = game;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        yourTurn = new Image(new Texture(Gdx.files.internal("yourTurn.png")));
        theirTurn = new Image(new Texture(Gdx.files.internal("theirTurn.png")));


        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);


        Table mainTable = new Table();
        mainTable.top().padTop(10);

        mainTable.add(yourTurn).pad(10).right();
        mainTable.add(theirTurn).pad(10).left();


        ImageButton newGameBtn = createImageButton("newGame.png");

        ImageButton joinGameBtn = createImageButton("joinGame.png");

        mainTable.add(newGameBtn).size(216,86).pad(10).right();
        mainTable.add(joinGameBtn).size(216,86).pad(10).left();

        root.add(mainTable).expand().top().row(); // push to top


        Texture navBarTexture = new Texture(Gdx.files.internal("NavBar.png"));
        Image navBarImage = new Image(navBarTexture);
        navBarImage.setWidth(Gdx.graphics.getWidth());

        Table navTable = new Table();
        navTable.setFillParent(true);
        navTable.bottom().padBottom(0);
        navTable.add(navBarImage).center();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            Actor hitbox = new Actor();
            hitbox.setBounds(64 * i, 0, 64, 64);
            hitbox.setTouchable(Touchable.enabled);

            hitbox.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switch (index) {
                        case 0: System.out.println("Home clicked"); break;
                        case 1: System.out.println("Trophy clicked"); break;
                        case 2: System.out.println("User clicked"); break;
                        case 3: System.out.println("Menu clicked"); break;
                    }
                }
            });

            stage.addActor(hitbox); // must be added AFTER navBarImage for z-index
        }

        stage.addActor(navTable);


    }

    private ImageButton createImageButton(String fileName) {
        Texture texture = new Texture(Gdx.files.internal(fileName));
        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
        return new ImageButton(drawable);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        stage.dispose();
        skin.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}



