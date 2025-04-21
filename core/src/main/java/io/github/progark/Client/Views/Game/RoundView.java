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
import io.github.progark.Server.Service.AuthService;

public class RoundView extends View {

    private final RoundController controller;
    private final Skin skin;
    private boolean initialized = false;

    private final Texture backgroundTexture;
    private final Texture quitTexture;

    private Label timerLabel;
    private Label scoreLabel;
    private Label categoryLabel;
    private TextField inputField;
    private TextButton submitButton;

    private Table answerContainer;
    private BitmapFont smallFont, largeFont;

    private String question = "No question available";

    public RoundView(RoundController gameController) {
        super();
        this.controller = gameController != null ? gameController : throwControllerNull();

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));
        this.quitTexture = new Texture(Gdx.files.internal("Quit.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter smallParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallParam.size = 20;
        this.smallFont = generator.generateFont(smallParam);

        FreeTypeFontGenerator.FreeTypeFontParameter largeParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        largeParam.size = 32;
        this.largeFont = generator.generateFont(largeParam);

        generator.dispose();

        try {
            String q = controller.getQuestion();
            if (q != null && !q.trim().isEmpty()) {
                this.question = q.trim();
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to retrieve question: " + e.getMessage());
        }
    }

    private RoundController throwControllerNull() {
        throw new IllegalArgumentException("RoundController cannot be null.");
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
        ImageButton quitButton = new ImageButton(new TextureRegionDrawable(quitTexture));
        quitButton.setSize(100, 100);
        quitButton.setPosition(30, Gdx.graphics.getHeight() - 130);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                forceEndRoundEarly();
            }
        });

        Label timerLabelTop = new Label("⏱ 60", new Label.LabelStyle(largeFont, Color.WHITE));
        timerLabelTop.setFontScale(1.5f);
        this.timerLabel = timerLabelTop;

        topBar.add(quitButton).left().padRight(20);
        topBar.add(timerLabelTop).expandX().center();
        root.add(topBar).expandX().fillX().row();

        // Score label
        scoreLabel = new Label("Score: 0", new Label.LabelStyle(largeFont, Color.WHITE));
        scoreLabel.setAlignment(Align.center);
        root.add(scoreLabel).padTop(10).row();

        // Category / Question
        categoryLabel = new Label(question, new Label.LabelStyle(largeFont, Color.WHITE));
        categoryLabel.setAlignment(Align.center);
        root.add(categoryLabel).padTop(20).padBottom(20).row();

        // Answer list
        answerContainer = new Table();
        ScrollPane scrollPane = new ScrollPane(answerContainer, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        root.add(scrollPane).expand().fill().padBottom(20).row();

        // Input row
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
                submitInputSafely();
            }
        });

        inputField.setTextFieldListener((field, c) -> {
            if (c == '\r' || c == '\n') {
                submitInputSafely();
            }
        });

        inputRow.add(inputField).expandX().fillX().padRight(10).height(80);
        inputRow.add(submitButton).width(160).height(80);
        root.add(inputRow).fillX().padBottom(10).row();

        stage.setKeyboardFocus(inputField);
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    private void submitInputSafely() {
        if (inputField == null || controller == null) return;
        String input = inputField.getText();
        if (input != null && !input.trim().isEmpty()) {
            inputField.setText("");
            controller.handleAnswerSubmission(input);
        }
    }

    @Override
    public void update(float delta) {
        if (controller != null) {
            controller.updateGameState(delta);
        }
        stage.act(delta);
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void updateSubmittedAnswers(Map<String, Integer> submittedAnswers) {
        if (answerContainer == null) {
            System.err.println("❌ Tried to update submitted answers before initialization.");
            return;
        }

        answerContainer.clear();

        if (submittedAnswers == null || submittedAnswers.isEmpty()) return;

        for (Map.Entry<String, Integer> entry : submittedAnswers.entrySet()) {
            String answer = entry.getKey();
            Integer points = entry.getValue();

            if (answer == null || points == null) {
                System.err.println("⚠️ Skipping null answer or points");
                continue;
            }

            try {
                Table row = new Table();
                row.left();

                Label.LabelStyle answerStyle = new Label.LabelStyle(largeFont, Color.WHITE);
                Label.LabelStyle pointStyle = new Label.LabelStyle(largeFont, points > 0 ? Color.GREEN : Color.LIGHT_GRAY);

                Label answerLabel = new Label(answer, answerStyle);
                Label pointLabel = new Label((points > 0 ? "+" : "") + points, pointStyle);
                pointLabel.setAlignment(Align.right);

                row.add(answerLabel).expandX().left().padRight(20);
                row.add(pointLabel).right();

                answerContainer.add(row).fillX().padBottom(15).row();
            } catch (Exception e) {
                System.err.println("❌ Failed to layout row: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void updateTimeRemaining(float time) {
        if (timerLabel != null) {
            timerLabel.setText("⏱ " + (int) time);
        }
    }

    public void showMessage(String displayMessage) {
        if (displayMessage != null && !displayMessage.trim().isEmpty()) {
        }
    }

    public void showGameOver() {
        showMessage("⏱ Time's up!");
    }

    public void forceEndRoundEarly(){
        controller.endRoundEarly();
        controller.goToGame();
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (quitTexture != null) quitTexture.dispose();
        if (skin != null) skin.dispose();
        if (smallFont != null) smallFont.dispose();
        if (largeFont != null) largeFont.dispose();
        super.dispose();
    }
}
