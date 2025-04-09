package io.github.progark.Server.Model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundModel {

    private final Map<String, Integer> playerOneAnswers = new HashMap<>();
    private final Map<String, Integer> playerTwoAnswers = new HashMap<>();

    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private float timeRemaining = 60f;

    private final QuestionModel question;

    private List<String> hasPlayedList = new ArrayList<>();

    public RoundModel(QuestionModel question) {
        this.question = question;

        if (question == null || question.getAnswer() == null) {
            throw new IllegalArgumentException("Invalid question or missing answers.");
        }
    }

    public boolean hasAlreadySubmitted(String answer) {
        String normalized = answer.toLowerCase();
        if (hasPlayedList.isEmpty()) {
            return playerOneAnswers.containsKey(normalized);
        } else {
            return playerTwoAnswers.containsKey(normalized);
        }
    }

    public boolean submitAnswer(String answer) {
        String normalized = answer.toLowerCase();

        if (hasAlreadySubmitted(normalized)) return false;

        int points = getPoints(normalized);

        if (hasPlayedList.isEmpty()) {
            playerOneAnswers.put(normalized, points);
            playerOneScore += points;
        } else {
            playerTwoAnswers.put(normalized, points);
            playerTwoScore += points;
        }

        return true;
    }

    private Integer getPoints(String answer) {
        Map<String, Integer> validAnswers = question.getAnswer();
        return validAnswers.getOrDefault(answer.toLowerCase(), 0);
    }

    public Map<String, Integer> getPlayerOneAnswers() {
        return playerOneAnswers;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
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

    public String getQuestion() {
        return question.getQuestion() != null ? question.question : "Unknown Question";
    }

    public Map<String, Integer> getPlayerTwoAnswers() {
        return playerTwoAnswers;
    }

    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    public void setPlayerTwoScore(int playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    public Map<String, Integer> getAllValidAnswers() {
        return question.getAnswer();
    }
}
