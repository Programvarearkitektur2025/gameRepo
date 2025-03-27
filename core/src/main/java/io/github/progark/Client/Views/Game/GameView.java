package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import io.github.progark.Client.Controllers.GameController;
import io.github.progark.Client.Model.GameModel;
import io.github.progark.Client.Model.UserModel;
import io.github.progark.Client.Service.GameService;
import io.github.progark.Client.Views.View;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.progark.Main;


import java.util.List;
import java.util.Map;

public class GameView extends View implements Screen {

    private final Main game;

    //private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;

    private Label timerLabel;
    private Label scoreLabel;
    private Label categoryLabel;
    private TextField inputField;
    private TextButton submitButton;

    private Table answerContainer;
    private com.badlogic.gdx.scenes.scene2d.ui.List<String> answersList;

    private GameModel gameModel;
    private GameController controller;
    private GameService gameService;
    private UserModel user1;
    private UserModel user2;


    public GameView(Main game) {
        // Initialize variables
        super(); // This calls view constructor for standard initialization of view.
        this.game = game;

        // Initialize texture and skin here. This needs to be correct to planned UI
        backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Initializing with dummy values, needs to be fixed.
        user1 = new UserModel("1","bastetest@test.com", "bastetest1");
        user2 = new UserModel("2","bastetest@test.com", "bastetest2");

        gameModel = new GameModel(user1, user2);
        gameService = new GameService(null, null);
        controller = new GameController(gameService, gameModel, this);
    }

    /*@Override
    protected void initialize() {
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.pad(40); // Add general padding
        stage.addActor(root);

        // Top labels
        categoryLabel = new Label("Category: Fruits", skin);
        timerLabel = new Label("Time: 60", skin);
        scoreLabel = new Label("Score: 0", skin);

        categoryLabel.setFontScale(2f);
        timerLabel.setFontScale(2f);
        scoreLabel.setFontScale(2f);

        // Input field
        inputField = new TextField("", skin);
        inputField.setMessageText("Enter answer...");
        inputField.setFocusTraversal(false);
        inputField.setSize(600, 80); // Set larger size
        inputField.getStyle().font.getData().setScale(1.5f);

        // Submit button
        submitButton = new TextButton("Submit", skin);
        submitButton.getLabel().setFontScale(1.8f);
        submitButton.getLabelCell().pad(10);
        submitButton.setHeight(80);

        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                submitAnswer();
            }
        });

        inputField.setTextFieldListener((textField, c) -> {
            if (c == '\r' || c == '\n') {
                submitAnswer();
            }
        });

        // Answer list
        answersList = new com.badlogic.gdx.scenes.scene2d.ui.List<>(skin);
        answersList.getStyle().font.getData().setScale(1.3f);

        ScrollPane scrollPane = new ScrollPane(answersList, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        // UI Layout
        root.top();
        root.add(categoryLabel).expandX().left().padBottom(20);
        root.add(timerLabel).expandX().center().padBottom(20);
        root.add(scoreLabel).expandX().right().padBottom(20);
        root.row();

        root.add(inputField).colspan(2).fillX().pad(10).height(80);
        root.add(submitButton).width(150).pad(10);
        root.row();

        root.add(scrollPane).colspan(3).fill().expand().padTop(20);
    }
    */

    @Override
    protected void initialize() {
        Gdx.input.setInputProcessor(stage);

        // === Root table ===
        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(20);
        stage.addActor(root);

        // === TOP BAR ===
        Table topBar = new Table();
        TextButton backButton = new TextButton("<", skin);
        Label timerLabelTop = new Label("⏱ 60", skin); // You can update this every frame

        backButton.getLabel().setFontScale(2f);
        timerLabelTop.setFontScale(2f);

        topBar.add(backButton).left().padRight(20);
        topBar.add(timerLabelTop).expandX().center();
        root.add(topBar).expandX().fillX().row();

        this.timerLabel = timerLabelTop; // Assign to use in update()

        // === CATEGORY LABEL ===
        categoryLabel = new Label(gameModel.getCategoryTitle(), skin);
        categoryLabel.setFontScale(2.5f);
        categoryLabel.setAlignment(Align.center);
        root.add(categoryLabel).padTop(20).padBottom(20).row();

        // === ANSWER LIST (ScrollPane inside container) ===
        answerContainer = new Table();
        ScrollPane scrollPane = new ScrollPane(answerContainer, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        root.add(scrollPane).expand().fill().padBottom(20).row();

        // === INPUT ROW ===
        Table inputRow = new Table();

        inputField = new TextField("", skin);
        inputField.setMessageText("Answer...");
        inputField.getStyle().font.getData().setScale(1.5f);

        submitButton = new TextButton("Submit", skin);
        submitButton.getLabel().setFontScale(1.5f);
        submitButton.setHeight(80);
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                submitAnswer();
            }
        });

        inputField.setTextFieldListener((field, c) -> {
            if (c == '\r' || c == '\n') submitAnswer();
        });

        inputRow.add(inputField).expandX().fillX().padRight(10).height(80);
        inputRow.add(submitButton).width(160).height(80);
        root.add(inputRow).fillX().padBottom(10).row();

        // Focus keyboard on start
        stage.setKeyboardFocus(inputField);
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    @Override
    public void show() {

    }

    /**
     * Called from LibGDX automatically every frame.
     */
    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    /**
     * Your custom update method called from render().
     */
    @Override
    public void update(float delta) {
        controller.updateTime(delta);

        if (controller.isTimeUp()) {
            inputField.setDisabled(true);
            submitButton.setDisabled(true);
            timerLabel.setText("Time's up!");
        } else {
            timerLabel.setText("Time: " + (int) controller.getTimeRemaining());
        }

        stage.act(delta);
    }

    private void submitAnswer() {
        String input = inputField.getText();
        boolean accepted = controller.trySubmitAnswer(input);
        if (accepted) {
            updateUI();
        }
        inputField.setText("");
    }

    private void updateUI() {
        answerContainer.clear();

        Map<String, Integer> submittedAnswers = controller.getSubmittedAnswers();

        submittedAnswers.forEach((answer, points) -> {
            Table row = new Table();

            Label label = new Label(answer, skin);
            label.setFontScale(1.5f);
            label.setColor((points > 0) ? Color.GREEN : Color.LIGHT_GRAY);

            Label pointLabel = new Label((points > 0) ? ("+" + points) : ("" + points), skin);
            pointLabel.setFontScale(1.5f);
            pointLabel.setColor((points > 0) ? Color.GREEN : Color.LIGHT_GRAY);

            row.add(label).left().expandX();
            row.add(pointLabel).right().padLeft(10);

            answerContainer.add(row).padBottom(10).row();
        });
    }


    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void enter() {
        super.enter();
    }

    @Override
    public void dispose() {
        // Clean up resources
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        // Dispose of other resources
        super.dispose(); // Call parent's dispose to clean up stage and spriteBatch

    }

}
