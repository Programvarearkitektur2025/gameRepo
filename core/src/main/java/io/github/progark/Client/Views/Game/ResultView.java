package io.github.progark.Client.Views.Game;

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
        }
    }
