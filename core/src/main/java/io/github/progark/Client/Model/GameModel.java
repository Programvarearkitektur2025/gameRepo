package io.github.progark.Client.Model;

import java.util.*;

public class GameModel {

    private final PlayerModel playerOne;
    private final PlayerModel playerTwo;

    private final Map<String, Integer> submittedAnswers = new HashMap<>();

    private int score = 0;
    private float timeRemaining = 60f;

    private final CategoryData.Category category;

    private String categoryId;

    public GameModel(UserModel user1, UserModel user2) {
        this.playerOne = new PlayerModel(user1);
        this.playerTwo = new PlayerModel(user2);

        categoryId = "2";

        this.category = CategoryData.getCategory(categoryId);

        if (this.category == null) {
            throw new IllegalArgumentException("Invalid category ID: " + categoryId);
        }
    }

    public PlayerModel getPlayerOne() { return playerOne; }
    public PlayerModel getPlayerTwo() { return playerTwo; }

    public boolean isAnswerValid(String answer) {
        return category.isCorrect(answer);
    }

    public boolean hasAlreadySubmitted(String answer) {
        return submittedAnswers.keySet().contains(answer.toLowerCase());
    }

    public void submitAnswer(String answer) {
        String normalized = answer.toLowerCase();

        if (hasAlreadySubmitted(normalized)) return;

        int points = getPoints(normalized);

        submittedAnswers.put(normalized, points);
        score += points;
    }

    private Integer getPoints(String answer){
        return category.getPoints(answer);
    }

    public Map<String, Integer> getSubmittedAnswers() {
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

    public String getCategoryTitle() {
        return category.title;
    }

    public Map<String, Integer> getAllValidAnswers() {
        return category.getAllAnswers();
    }
}
