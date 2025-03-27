package io.github.progark.Client.Controllers;

import io.github.progark.Server.Model.Game;

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
        if (answer.isEmpty() || model.hasAlreadySubmitted(answer)) {
            return false;
        }

        model.submitAnswer(answer);
        return true;
    }

    public void updateTime(float delta) {
        model.updateTime(delta);
    }

    public boolean isTimeUp() {
        return model.isTimeUp();
    }

    public int getScore() {
        return model.getScore();
    }

    public float getTimeRemaining() {
        return model.getTimeRemaining();
    }

    public java.util.List<String> getSubmittedAnswers() {
        return model.getSubmittedAnswers();
    }

}

