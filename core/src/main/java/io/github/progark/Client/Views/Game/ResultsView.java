package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import io.github.progark.Client.Views.View;
import io.github.progark.Main;

public class ResultsView extends View {

    private final Main game;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture profileTexture;
    private final Texture continueButtonTexture;
    private final Texture solutionButtonTexture;
    private final Texture textFieldTexture;
    private final Image background;
    private final BitmapFont font;

    public ResultsView(Main game) {
        super();
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        profileTexture = new Texture(Gdx.files.internal("Person.png"));
        continueButtonTexture = new Texture(Gdx.files.internal("ContinueButton.png"));
        solutionButtonTexture = new Texture(Gdx.files.internal("SolutionButton.png"));
        textFieldTexture = new Texture(Gdx.files.internal("TextField.png"));

        background = new Image(backgroundTexture);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    protected void initialize() {
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(40);
        stage.addActor(table);

        // Runde-tittel
        Label roundLabel = new Label("ROUND 1", new Label.LabelStyle(font, Color.WHITE));
        roundLabel.setFontScale(2.5f);
        table.add(roundLabel).padBottom(50).padTop(150).row();

        // Profilbilder og score
        Table scoreRow = new Table();
        Image leftProfile = new Image(profileTexture);
        Image rightProfile = new Image(profileTexture);
        Label scoreLabel = new Label("0 - 1", new Label.LabelStyle(font, Color.WHITE));

        scoreRow.add(leftProfile).size(120).padRight(40);
        scoreRow.add(scoreLabel).padRight(40);
        scoreRow.add(rightProfile).size(120);
        table.add(scoreRow).padTop(60).padBottom(100).row();

        // Resultatboks med prompt og svar (bruker TextField.png som bakgrunn)
        Drawable textFieldBackground = new TextureRegionDrawable(textFieldTexture);
        Image boxBackground = new Image(textFieldBackground);

        Stack resultBox = new Stack();
        resultBox.add(boxBackground);

        Table resultTable = new Table();
        resultTable.top().padTop(30);
        Label promptLabel = new Label("Girlsname starting with ‘L’", new Label.LabelStyle(font, Color.DARK_GRAY));
        promptLabel.setAlignment(Align.center);
        resultTable.add(promptLabel).padBottom(30).row();

        // Eksempelinnhold
        for (int i = 0; i < 4; i++) {
            Table row = new Table();
            row.add(new Label("Lilly", new Label.LabelStyle(font, Color.DARK_GRAY))).left().padRight(20);
            row.add(new Label("1+", new Label.LabelStyle(font, Color.FOREST))).right().padRight(40);
            row.add(new Label("Loura", new Label.LabelStyle(font, Color.DARK_GRAY))).left();
            row.add(new Label("1+", new Label.LabelStyle(font, Color.FOREST))).right();
            resultTable.add(row).padBottom(15).row();
        }

        resultBox.add(resultTable);
        table.add(resultBox).width(850).height(1050).padBottom(40).row();

        // Total-score
        Label totalScore = new Label("3   TOTAL   5", new Label.LabelStyle(font, Color.DARK_GRAY));
        table.add(totalScore).padBottom(40).row();

        // Knapper nederst
        Table buttons = new Table();
        ImageButton solutionButton = new ImageButton(new TextureRegionDrawable(solutionButtonTexture));
        ImageButton continueButton = new ImageButton(new TextureRegionDrawable(continueButtonTexture));

        buttons.add(solutionButton).size(400, 300).padRight(40);
        buttons.add(continueButton).size(400, 300);
        table.add(buttons);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        profileTexture.dispose();
        continueButtonTexture.dispose();
        solutionButtonTexture.dispose();
        textFieldTexture.dispose();
        font.dispose();
    }
}
