package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import io.github.progark.Client.Controllers.LeaderBoardController;
import io.github.progark.Client.Views.View;
import io.github.progark.Client.Views.Components.NavBar;

public class LeaderBoardView extends View {

    private Texture backgroundTexture;
    private final LeaderBoardController controller;
    private NavBar navBar;

    public LeaderBoardView(LeaderBoardController controller) {
        super();
        this.controller = controller;
        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        enter();
    }

    @Override
    protected void initialize() {
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);
        navBar = new NavBar(stage, controller.getMain());
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
    }
}
