package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import io.github.progark.Client.Model.ResultModel;

//PLACEHOLDER RESULTVIEW, MÅ ENDRES
public class ResultView implements Screen {
    private Stage stage;
    private Skin skin;
    private Label resultLabel;
    private ResultModel resultModel;

    public ResultView() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        resultLabel = new Label("Resultater", skin);


        resultLabel.setPosition(100, 100);


        stage.addActor(resultLabel);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
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
        stage.dispose();
        skin.dispose();
    }


    public void update(ResultModel updatedModel) {
        this.resultModel = updatedModel;

        resultLabel.setText("Vinner: " + updatedModel.getWinner());
    }
}
