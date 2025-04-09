package io.github.progark.Client.Controllers;

import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Service.RoundService;
import io.github.progark.Client.Views.Game.RoundView;
import io.github.progark.Server.Service.SolutionService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class RoundController extends Controller {
    private RoundService gameService;
    private SolutionService solutionService;
    private RoundModel roundModel;
    private RoundView gameView;
    // Dette er variabler jeg har initiert og som må dobbeltsjekkes mot ditt arbeid Stian
    private GameModel parentGameModel;
    private Main main;


    public RoundController(GameModel gameModel, DatabaseManager databaseManager, Main main) {
        this.solutionService = new SolutionService(databaseManager);
        this.gameView= new RoundView(this);
        this.parentGameModel = gameModel;
        this.main = main;
        enter();
    }

    public void setGameView(RoundView gameView) {
        this.gameView = gameView;
    }

    // Submit user input to game model

    public void handleAnswerSubmission(String input) {
        String answer = input.trim().toLowerCase();
        if (answer.isEmpty() || roundModel.hasAlreadySubmitted(answer)) {
            gameView.showMessage("Invalid answer. Please try again.");
            return;
        }
        boolean success = roundModel.submitAnswer(answer);
        if (success) {
            gameView.updateScore(roundModel.getPlayerOneScore());
            gameView.updateSubmittedAnswers(roundModel.getPlayerOneAnswers());
        }
    }


    public void updateGameState(float delta) {
        roundModel.updateTime(delta);
        gameView.updateTimeRemaining(roundModel.getTimeRemaining());

        if (roundModel.isTimeUp()) {
            gameView.showGameOver();
        }
    }

    public boolean isTimeUp() {
        return roundModel.isTimeUp();
    }

    public int getScore() {
        return roundModel.getPlayerOneScore();
    }

    public float getTimeRemaining() {
        return roundModel.getTimeRemaining();
    }

    public void updateTime(float delta){
        // Add logic as necessary
    };

    public boolean submitAnswer(String answer){
        boolean isAnswerCorrect = roundModel.submitAnswer(answer);
        return isAnswerCorrect;
    }

    public java.util.Map<String, Integer> getSubmittedAnswers() {
        return roundModel.getPlayerOneAnswers();
    }

    public void getQuestionByID(String ID) {
        solutionService.getQuestion(ID, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                System.out.println(data);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Stupid: "+ e);
                return;
            }
        });
    }

    // Function should not take anything and should return the question for the given round.
    // Used by RoundView to initialize question to display
    public String getQuestion(){
        String question = roundModel.getQuestion();
        return question;
    }

    // Function that takes a roundModel object that is populated and sets it in the parentGameModel
    // and sends the user back to the gameView through the controller. This function is called from
    // roundview whenever a round is finished.
    public void returnToGameView(RoundModel roundModel){
        parentGameModel.setFinishedRound(roundModel);
        main.useGameController(parentGameModel);
    }

    @Override
    public void enter() {
        gameView.enter();
    }

    @Override
    public void update(float delta) {
        gameView.update(delta);
        //view.handleInput();
        gameView.render();
    }

    @Override
    public void dispose() {
        gameView.dispose();
    }
}

