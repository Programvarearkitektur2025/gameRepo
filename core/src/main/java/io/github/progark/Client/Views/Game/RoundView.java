package io.github.progark.Client.Views.Game;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import io.github.progark.Client.Views.View;
import io.github.progark.Client.Controllers.RoundController;
import io.github.progark.Server.Model.Game.RoundModel;

public class RoundView extends View {

    private final RoundController controller;
    //private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;
    private Label timerLabel;
    private Label scoreLabel;
    private Label categoryLabel;
    private TextField inputField;
    private TextButton submitButton;

    private Table answerContainer;
    private List<String> answers;

    // Variable for displaying question
    private String question;



    public RoundView(RoundController gameController){
        super(); // This calls view constructor for standard initialization of view.
        //GameService gameService = new GameService(game.getDatabaseManager());

        this.controller = gameController;
        //this.game = game;

        // Initialize texture and skin here. This needs to be correct to planned UI
        //backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        //this.game = game;

        backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));
        //skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Initializing with dummy values, needs to be fixed.
        //user1 = new UserModel("1","bastetest@test.com", "bastetest1");
        //user2 = new UserModel("2","bastetest@test.com", "bastetest2");
        question = controller.getQuestion();
    }

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
        // === SAME AS QUESTION? ===
        categoryLabel = new Label(question, skin);
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
                //submitAnswer();
            }
        });

        inputField.setTextFieldListener((field, c) -> {
            if (c == '\r' || c == '\n') {
                return;
                // Her må logikk for å kalle submitAnswer-funksjon være
                //submitAnswer();
            }
        });

        inputRow.add(inputField).expandX().fillX().padRight(10).height(80);
        inputRow.add(submitButton).width(160).height(80);
        root.add(inputRow).fillX().padBottom(10).row();

        // Focus keyboard on start
        stage.setKeyboardFocus(inputField);
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    /**
     * Called from LibGDX automatically every frame.
     */

    /**
     * Your custom update method called from render().
     */
    @Override
    public void update(float delta) {
        /*
        controller.updateTime(delta);


        if (controller.isTimeUp()) {
            inputField.setDisabled(true);
            submitButton.setDisabled(true);
            timerLabel.setText("Time's up!");
        } else {
            timerLabel.setText("Time: " + (int) controller.getTimeRemaining());
        }



       stage.act(delta);

         */
    }

    // Takes a string and should update the UI based on if the answer is accepted or not
    // is called whenever a user submits an answer
    private void submitAnswer(String answer) {
        boolean isAnswerCorrect = controller.submitAnswer(answer);
        if (isAnswerCorrect) {
            // Logic for updating UI with correct answer.
            // updateUI(); Not necessary correct function
        }else{
            // Logic for updating UI with incorrect answer.
            // updateUI(); Not necessary correct function
        }
        /*
        String input = inputField.getText();
        boolean accepted = controller.trySubmitAnswer(input);
        if (accepted) {
            updateUI();
        }
        inputField.setText("");
        */
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

    // Function that should take a RoundModel object and send it to the gameController for updated
    // gameView with the information of the newly executed round.
    public void roundIsFinished(RoundModel roundModel){
        controller.returnToGameView(roundModel);
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

    public void showMessage(String displayMessage){
        // Logic for displaying error message
        // Method should end with an call to the render method to render new UI
    }
    public void updateScore (int score){
        // Logic for displaying score
        // Method should end with an call to the render method to render new UI
    }

    public void updateSubmittedAnswers(Map<String,Integer> solution){
        // Logic for updating submitted answers
        // Method should end with an call to the render method to render new UI

    }
    public void updateTimeRemaining(float time){
    // Logic for updating the time remaining
    // Method should end with an call to the render method to render new UI
    }

    public void showGameOver(){
    // Logic for displaying that the game is over.
    // Method should end with an call to the render method to render new UI
    }
    /*
    @Override
    public void render(SpriteBatch sb) {

    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void handleInput() {
    }
        */

}
