package io.github.progark.Client.Views.Game;

<<<<<<< HEAD
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;

import io.github.progark.Client.Model.ResultModel;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;

//Eksempel på ResultView
public class ResultView extends View {
    private final Main game;
    private ResultModel resultModel;

    private Skin skin;
    private Texture backgroundTexture;
    private Image background;
    private BitmapFont bigFont;
    private Label resultLabel;

    public ResultView(Main game, ResultModel resultModel) {
        super();
        this.game = game;
        this.resultModel = resultModel;
    }


    protected void initialize() {

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));


        bigFont = new BitmapFont(Gdx.files.internal("fonts/big.fnt"));


        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);


        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bigFont;


        String displayText = buildResultText(resultModel);

        resultLabel = new Label(displayText, labelStyle);
        resultLabel.setFontScale(1.0f);
        resultLabel.setAlignment(1);


        table.center();
        table.add(resultLabel).pad(50);
    }

   //Eksempel på hvordan man kan hente ut verdier fra Result
    private String buildResultText(ResultModel model) {
        if (model == null) {
            return "No results yet";
        }


        return "RESULTATER\n\n" +
            "Vinner: " + model.getWinner() + " (score: " + model.getWinnerScore() + ")\n" +
            "Taper:  " + model.getLoser()  + " (score: " + model.getLoserScore()  + ")\n\n" +
            "Spørsmål: " + model.getQuestions() + "\n\n" +
            "Antall gjetninger P1: " + model.getNumberOfGuessesP1() + "\n" +
            "Antall gjetninger P2: " + model.getNumberOfGuessesP2() + "\n\n" +
            "Korrekte gjetninger P1: " + model.getCorrectGuessesP1() + "\n" +
            "Korrekte gjetninger P2: " + model.getCorrectGuessesP2() + "\n\n" +
            "Gjetninger P1: " + model.getGuessesP1() + "\n" +
            "Gjetninger P2: " + model.getGuessesP2() + "\n";
    }


    @Override
    public void update(float dt) {

    }
    public void updateResultModel(ResultModel model) {
        this.resultModel = model;

        if (resultLabel != null) {
            resultLabel.setText(buildResultText(model));
        }
    }
    @Override
    public void dispose() {
        super.dispose();
        if (skin != null) {
            skin.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (bigFont != null) {
            bigFont.dispose();
        }
=======
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
//PLACEHOLDER RESULTVIEW, MÅ ENDRES
public class ResultView implements Screen {
    private Stage stage;
    private Skin skin;
    private Label resultLabel;

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
>>>>>>> fba5587 (Rsultmodel)
    }
}
