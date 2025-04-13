package io.github.progark.Client.Views.Game;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import io.github.progark.Client.Views.View;
import io.github.progark.Client.Controllers.RoundController;
import io.github.progark.Server.Model.Game.RoundModel;

public class RoundView extends View {

    private final RoundController controller;
    private final Skin skin;
    private boolean initialized = false;

    private final Texture backgroundTexture;
    private final Texture backButtonTexture;

    private Label timerLabel;
    private Label scoreLabel;
    private Label categoryLabel;
    private TextField inputField;
    private TextButton submitButton;

    private Table answerContainer;
    private BitmapFont smallFont, largeFont;

    private String question;

    public RoundView(RoundController gameController) {
        super();
        this.controller = gameController;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));
        backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter smallParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallParam.size = 20;
        this.smallFont = generator.generateFont(smallParam);

        FreeTypeFontGenerator.FreeTypeFontParameter largeParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        largeParam.size = 32;
        this.largeFont = generator.generateFont(largeParam);

        generator.dispose();

        question = controller.getQuestion();
    }

    @Override
    protected void initialize() {
        if (initialized) return;
        initialized = true;
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(20);
        stage.addActor(root);

        // Top bar
        Table topBar = new Table();
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(100, 100);
        backButton.setPosition(30, Gdx.graphics.getHeight() - 130);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goToGame();
            }
        });

        Label timerLabelTop = new Label("⏱ 60", new Label.LabelStyle(largeFont, Color.WHITE));
        timerLabelTop.setFontScale(1.5f);

        topBar.add(backButton).left().padRight(20);
        topBar.add(timerLabelTop).expandX().center();
        root.add(topBar).expandX().fillX().row();
        this.timerLabel = timerLabelTop;

        // Score
        scoreLabel = new Label("Score: 0", new Label.LabelStyle(largeFont, Color.WHITE));
        scoreLabel.setAlignment(Align.center);
        root.add(scoreLabel).padTop(10).row();

        // Question
        categoryLabel = new Label(question, new Label.LabelStyle(largeFont, Color.WHITE));
        categoryLabel.setAlignment(Align.center);
        root.add(categoryLabel).padTop(20).padBottom(20).row();

        // Answer list
        answerContainer = new Table();
        ScrollPane scrollPane = new ScrollPane(answerContainer, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        root.add(scrollPane).expand().fill().padBottom(20).row();

        // Input field + button
        Table inputRow = new Table();
        inputField = new TextField("", skin);
        inputField.setMessageText("Answer...");
        inputField.setStyle(skin.get(TextField.TextFieldStyle.class));
        inputField.getStyle().font = smallFont;

        submitButton = new TextButton("Submit", skin);
        submitButton.getLabel().setFontScale(1.5f);
        submitButton.setHeight(80);

        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String input = inputField.getText();
                inputField.setText("");
                controller.handleAnswerSubmission(input);
            }
        });

        inputField.setTextFieldListener((field, c) -> {
            if (c == '\r' || c == '\n') {
                String input = inputField.getText();
                inputField.setText("");
                controller.handleAnswerSubmission(input);
            }
        });

        inputRow.add(inputField).expandX().fillX().padRight(10).height(80);
        inputRow.add(submitButton).width(160).height(80);
        root.add(inputRow).fillX().padBottom(10).row();

        stage.setKeyboardFocus(inputField);
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    @Override
    public void update(float delta) {
        controller.updateGameState(delta);
        stage.act(delta);
    }

    public void updateScore(int score) {
        System.out.println("New score:" + score);
        scoreLabel.setText("Score: " + score);
    }

    public void updateSubmittedAnswers(Map<String, Integer> submittedAnswers) {
        answerContainer.clear();
        if (submittedAnswers == null || submittedAnswers.isEmpty()) return;

        // Use the same working font as score/timer
        BitmapFont customFont = largeFont;

        submittedAnswers.forEach((answer, points) -> {
            Table row = new Table();
            row.left();

            // Label style for answer text (always white)
            Label.LabelStyle answerStyle = new Label.LabelStyle();
            answerStyle.font = customFont;
            answerStyle.fontColor = Color.WHITE;

            // Label style for points (green or gray)
            Label.LabelStyle pointStyle = new Label.LabelStyle();
            pointStyle.font = customFont;
            pointStyle.fontColor = points > 0 ? Color.GREEN : Color.LIGHT_GRAY;

            Label answerLabel = new Label(answer, answerStyle);
            Label pointLabel = new Label((points > 0 ? "+" : "") + points, pointStyle);
            pointLabel.setAlignment(Align.right);

            row.add(answerLabel).expandX().left().padRight(20);
            row.add(pointLabel).right();

            answerContainer.add(row).fillX().padBottom(15).row();
        });
    }



    public void updateTimeRemaining(float time) {
        timerLabel.setText("⏱ " + (int) time);
    }

    public void roundIsFinished(RoundModel roundModel) {
        controller.returnToGameView(roundModel);
    }

    public void showMessage(String displayMessage) {
        System.out.println("MESSAGE: " + displayMessage);
    }

    public void showGameOver() {
        showMessage("⏱ Time's up!");
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (skin != null) skin.dispose();
        if (smallFont != null) smallFont.dispose();
        if (largeFont != null) largeFont.dispose();
        super.dispose();
    }
}
