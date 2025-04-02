package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import io.github.progark.Client.Views.View;
import io.github.progark.Client.Views.Components.NavBar;
import io.github.progark.Client.Controllers.LeaderboardController;

public class LeaderBoardView extends View {

    private Texture backgroundTexture;
    private final LeaderboardController controller;
    private NavBar navBar;
    private Skin skin;

    public LeaderBoardView(LeaderboardController controller) {
        super();
        this.controller = controller;
        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));

    }

    @Override
    protected void initialize() {
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        Label label = new Label("leaderboardpage", skin);
        label.setFontScale(2f);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(100);
        table.add(label);

        stage.addActor(table);

        navBar = new NavBar(stage, controller.getMain());
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        skin.dispose();
        navBar.dispose();
    }
}
