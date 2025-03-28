package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;

public class CreateGameView extends View {
    private final Main game;
    private final Skin skin;

    public CreateGameView(Main game) {
        super();
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    @Override
    protected void initialize() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Create Game", skin);
        titleLabel.setFontScale(2f);

        TextButton createButton = new TextButton("Create", skin);
        createButton.getLabel().setFontScale(1.5f);

        table.add(titleLabel).padBottom(30).row();
        table.add(createButton).width(200).height(50).row();

        // Add button listener
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.getViewManager().setView(() -> new GameView(game));
            }
        });

    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }
}
