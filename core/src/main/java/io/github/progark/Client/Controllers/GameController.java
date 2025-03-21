package io.github.progark.Client.Controllers;

import io.github.progark.Server.Model.Game;

public class GameController {
    private final Game model;

    public GameController(Game model) {
        this.model = model;
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

