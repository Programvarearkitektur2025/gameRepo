package io.github.progark.Server.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private List<String> submittedAnswers = new ArrayList<>();
    private List<String> validAnswers = Arrays.asList("apple", "banana", "mango", "grape", "orange");
    private int score = 0;
    private float timeRemaining = 60f;

    public boolean isAnswerValid(String answer) {
        return validAnswers.contains(answer.toLowerCase());
    }

    public boolean hasAlreadySubmitted(String answer) {
        return submittedAnswers.contains(answer.toLowerCase());
    }

    public void submitAnswer(String answer) {
        String normalized = answer.toLowerCase();
        submittedAnswers.add(normalized);
        if (isAnswerValid(normalized)) {
            score += 10;
        }
    }

    public List<String> getSubmittedAnswers() {
        return submittedAnswers;
    }

    public int getScore() {
        return score;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public void updateTime(float delta) {
        timeRemaining = Math.max(0, timeRemaining - delta);
    }

    public boolean isTimeUp() {
        return timeRemaining <= 0;
    }
}
