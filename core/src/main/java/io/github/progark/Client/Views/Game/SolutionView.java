package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.progark.Client.Controllers.SolutionController;
import io.github.progark.Client.Views.View;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

public class SolutionView extends View {

    private final SolutionController solutionController;
    private final Texture backgroundTexture, backButtonTexture, solutionTexture;
    private final Skin skin;
    private final BitmapFont font;

    public SolutionView(SolutionController solutionController) {
        super();
        this.solutionController = solutionController;

        // UI Components
        this.backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));
        this.solutionTexture = new Texture(Gdx.files.internal("Solution_Header.png"));

        // Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        font = generator.generateFont(parameter);
        generator.dispose();

        enter();
    }

    @Override
    protected void initialize() {
        // Bakgrunn
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Solution header
        Image header = new Image(solutionTexture);
        header.setSize(500, 100);
        header.setPosition(Gdx.graphics.getWidth() / 2 - header.getWidth() / 2, Gdx.graphics.getHeight() - 250); // Center the header horizontally
        header.setPosition(Gdx.graphics.getWidth() / 2 - header.getWidth() / 2, Gdx.graphics.getHeight() - 250); // Center the header horizontally

        // Get correct answers and points from SolutionController.
        // Dette må byttes ut med faktisk hashmap fra firebase fra SolutionController
        Map<String, Integer> correctAnswers = solutionController.getCorrectAnswers();
        int answerCount = 0;

        Table answersTable = new Table();

        for (Map.Entry<String, Integer> entry : correctAnswers.entrySet()) {
            // Viser max 10 svar
            if (answerCount >= 10) {
                break;
            }

            Table row = new Table();

            Label.LabelStyle labelStyle = new Label.LabelStyle(font, skin.getColor("white")); // Use the larger BitmapFont

            Label answerLabel = new Label(entry.getKey(), labelStyle); // Answer label

            row.add(answerLabel).left();
            answersTable.add(row).padBottom(15).row();

            answerCount++;
        }

        // Position the answers table in the center of the screen
        answersTable.setPosition(Gdx.graphics.getWidth() / 2 - answersTable.getWidth() / 2, Gdx.graphics.getHeight() - 650);
        stage.addActor(answersTable);

        // Tilbake knapp
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(100, 100);
        backButton.setPosition(30, Gdx.graphics.getHeight() - 130);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                solutionController.goBackToHome();
            }
        });

        stage.addActor(header);
        stage.addActor(backButton);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        backButtonTexture.dispose();
        skin.dispose();
    }

}

