package io.github.progark.Client.Controllers;

import io.github.progark.Main;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Service.RoundService;
import io.github.progark.Client.Views.Game.RoundView;
import io.github.progark.Server.Service.SolutionService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class RoundController extends Controller {
    private RoundService gameService;
    private SolutionService solutionService;
    private RoundModel gameModel;
    private RoundView gameView;

    public RoundController(DatabaseManager databaseManager, Main main) {
        this.solutionService = new SolutionService(databaseManager);
        this.gameView= new RoundView(this);
    }

    public void setGameView(RoundView gameView) {
        this.gameView = gameView;
    }

    // Submit user input to game model

    public void handleAnswerSubmission(String input) {
        String answer = input.trim().toLowerCase();
        if (answer.isEmpty() || gameModel.hasAlreadySubmitted(answer)) {
            gameView.showMessage("Invalid answer. Please try again.");
            return;
        }
        boolean success = gameModel.submitAnswer(answer);
        if (success) {
            gameView.updateScore(gameModel.getScore());
            gameView.updateSubmittedAnswers(gameModel.getSubmittedAnswers());
        }
    }


    public void updateGameState(float delta) {
        gameModel.updateTime(delta);
        gameView.updateTimeRemaining(gameModel.getTimeRemaining());

        if (gameModel.isTimeUp()) {
            gameView.showGameOver();
        }
    }

    public boolean isTimeUp() {
        return gameModel.isTimeUp();
    }

    public int getScore() {
        return gameModel.getScore();
    }

    public float getTimeRemaining() {
        return gameModel.getTimeRemaining();
    }

    public void updateTime(float delta){
        // Add logic as necessary
    };

    public boolean trySubmitAnswer(String answer){
        // Add logic as necessary
        return true;
    }

    public java.util.Map<String, Integer> getSubmittedAnswers() {
        return gameModel.getSubmittedAnswers();
    }

    public void getQuestionByID(String ID) {
        solutionService.getQuestion(ID, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                System.out.println(data);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Stupid");
                return;
            }
        });
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

