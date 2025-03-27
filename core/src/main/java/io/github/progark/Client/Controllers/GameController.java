package io.github.progark.Client.Controllers;

import io.github.progark.Client.Model.GameModel;
import io.github.progark.Client.Service.GameService;
import io.github.progark.Client.Views.Game.GameView;

public class GameController {
    private GameService gameService;
    private GameModel gameModel;
    private GameView gameView;

    public GameController(GameService gameService, GameModel gameModel, GameView gameView) {
        this.gameService = gameService;
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    public boolean trySubmitAnswer(String input) {
        String answer = input.trim().toLowerCase();
        if (answer.isEmpty() || gameModel.hasAlreadySubmitted(answer)) {
            return false;
        }

        gameModel.submitAnswer(answer);
        return true;
    }

    public void updateTime(float delta) {
        gameModel.updateTime(delta);
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

    public java.util.Map<String, Integer> getSubmittedAnswers() {
        return gameModel.getSubmittedAnswers();
    }

}

