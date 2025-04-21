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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import io.github.progark.Client.Views.View;
import io.github.progark.Client.Controllers.RoundController;

public class RoundView extends View {

    private final RoundController controller;
    private final Skin skin;
    private boolean initialized = false;

    private final Texture backgroundTexture;
    private final Texture quitTexture;
    private final Texture textFieldTexture;
    private final Texture enterTexture;
    private final Texture rectangleTexture;

    private Label timerLabel;
    private Label scoreLabel;
    private Label categoryLabel;
    private TextField inputField;
    private ImageButton submitButton;

    private Table answerContainer;
    private BitmapFont smallFont, largeFont, extraLargeFont;

    private String question = "No question available";

    public RoundView(RoundController gameController) {
        super();
        this.controller = gameController != null ? gameController : throwControllerNull();

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));
        this.quitTexture = new Texture(Gdx.files.internal("Quit.png"));
        this.textFieldTexture = new Texture(Gdx.files.internal("LargeRectangle.png"));
        this.enterTexture = new Texture(Gdx.files.internal("Enter.png"));
        this.rectangleTexture = new Texture(Gdx.files.internal("Rectangle.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter smallParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallParam.size = 35;
        this.smallFont = generator.generateFont(smallParam);

        FreeTypeFontGenerator.FreeTypeFontParameter largeParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        largeParam.size = 50;
        this.largeFont = generator.generateFont(largeParam);

        FreeTypeFontGenerator.FreeTypeFontParameter extraLargeParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        extraLargeParam.size = 55;
        this.extraLargeFont = generator.generateFont(extraLargeParam);

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
        root.top().padTop(20);
        stage.addActor(root);

        // Top bar
        Table topBar = new Table();
        ImageButton quitButton = new ImageButton(new TextureRegionDrawable(quitTexture));
        quitButton.setSize(260, 260);
        quitButton.setPosition(100, Gdx.graphics.getHeight() - 150);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                forceEndRoundEarly();
            }
        });

        Label timerLabelTop = new Label("Seconds left: ⏱ 60", new Label.LabelStyle(extraLargeFont, Color.WHITE));
        timerLabelTop.setFontScale(1.5f);
        this.timerLabel = timerLabelTop;

        topBar.add(quitButton).left().padRight(10).padLeft(80).height(260).width(260);
        topBar.add(timerLabelTop).expandX().padRight(85);
        root.add(topBar).expandX().fillX().row();

        // Score label
        scoreLabel = new Label("Score: 0", new Label.LabelStyle(largeFont, Color.WHITE));
        scoreLabel.setAlignment(Align.center);
        root.add(scoreLabel).padTop(10).padBottom(40).row();

        // Answer list with background and category inside
        answerContainer = new Table();
        ScrollPane scrollPane = new ScrollPane(answerContainer);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        // Stack with background and overlay (category at top)
        Stack answerStack = new Stack();
        Image backgroundImage = new Image(new TextureRegionDrawable(textFieldTexture));
        Table overlayTable = new Table();
        overlayTable.top().padTop(10);

        categoryLabel = new Label(question, new Label.LabelStyle(largeFont, Color.DARK_GRAY));
        categoryLabel.setWrap(true);
        categoryLabel.setAlignment(Align.center);
        overlayTable.add(categoryLabel).width(800).padTop(10).padBottom(10).center().row();
        overlayTable.add(scrollPane).expand().fill();

        answerStack.add(backgroundImage);
        answerStack.add(overlayTable);

        root.add(answerStack).expand().fill().padBottom(20).width(900).row();

        // Input row
        Table inputRow = new Table();

        Drawable inputBackground = new TextureRegionDrawable(rectangleTexture);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = smallFont;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = inputBackground;
        textFieldStyle.cursor = skin.newDrawable("white", Color.GRAY);

        inputField = new TextField("", textFieldStyle);
        inputField.setMessageText("Answer...");
        inputField.setAlignment(Align.left);

        submitButton = new ImageButton(new TextureRegionDrawable(enterTexture));
        submitButton.setSize(245, 245);

        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                submitInputSafely();
            }
        });

        inputField.setTextFieldListener((field, c) -> {
            if (c == '\r' || c == '\n') {
                submitInputSafely();
            }
        });

        inputRow.add(inputField).width(620).height(120).padRight(30).padBottom(460);
        inputRow.add(submitButton).width(245).height(245).padBottom(465);
        root.add(inputRow).padBottom(460).row();

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

                Label.LabelStyle answerStyle = new Label.LabelStyle(smallFont, Color.BLACK);
                Label.LabelStyle pointStyle = new Label.LabelStyle(smallFont, points > 0 ? Color.GREEN : Color.LIGHT_GRAY);

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
            timerLabel.setText("Seconds left: ⏱ " + (int) time);
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
        if (textFieldTexture != null) textFieldTexture.dispose();
        if (enterTexture != null) enterTexture.dispose();
        if (rectangleTexture != null) rectangleTexture.dispose();
        if (skin != null) skin.dispose();
        if (smallFont != null) smallFont.dispose();
        if (largeFont != null) largeFont.dispose();
        if (extraLargeFont != null) extraLargeFont.dispose();
        super.dispose();
    }
}
