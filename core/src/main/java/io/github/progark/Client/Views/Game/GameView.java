package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.progark.Client.Controllers.GameController;
import io.github.progark.Client.Views.View;
import io.github.progark.Server.Model.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.progark.Client.Views.View;
import io.github.progark.Main;


import java.util.List;

public class GameView extends View implements Screen {

    private Stage stage;
    private final Main game;

    //private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;

    private Label timerLabel;
    private Label scoreLabel;
    private Label categoryLabel;
    private TextField inputField;
    private TextButton submitButton;
    private com.badlogic.gdx.scenes.scene2d.ui.List<String> answersList;

    private Game gameModel;
    private GameController controller;

    public GameView(Main game){
        // Initialize variables
        super(); // This calls view constructor for standard initialization of view.
        this.game = game;

        // Initialize texture and skin here. This needs to be correct to planned UI
        backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        gameModel = new Game();
        controller = new GameController(gameModel);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        categoryLabel = new Label("Category: Fruits", skin);
        timerLabel = new Label("Time: 60", skin);
        scoreLabel = new Label("Score: 0", skin);

        inputField = new TextField("", skin);
        inputField.setMessageText("Enter answer...");
        inputField.setFocusTraversal(false);

        submitButton = new TextButton("Submit", skin);
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

        answersList = new com.badlogic.gdx.scenes.scene2d.ui.List<>(skin);
        ScrollPane scrollPane = new ScrollPane(answersList, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        root.top().pad(20);
        root.add(categoryLabel).expandX().left();
        root.add(timerLabel).expandX().center();
        root.add(scoreLabel).expandX().right();
        root.row().padTop(20);
        root.add(inputField).colspan(2).fillX().padRight(10);
        root.add(submitButton).width(100);
        root.row().padTop(20).expand().fill();
        root.add(scrollPane).colspan(3).fill().expand();
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
    public void update(float dt) {
        controller.updateTime(dt);

        if (controller.isTimeUp()) {
            inputField.setDisabled(true);
            submitButton.setDisabled(true);
            timerLabel.setText("Time's up!");
        } else {
            timerLabel.setText("Time: " + (int) controller.getTimeRemaining());
        }

        stage.act(dt);
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
        scoreLabel.setText("Score: " + controller.getScore());
        List<String> answers = controller.getSubmittedAnswers();
        answersList.setItems(answers.toArray(new String[0]));
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
    protected void initialize() {

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