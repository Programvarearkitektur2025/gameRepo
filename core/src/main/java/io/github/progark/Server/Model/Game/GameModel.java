package io.github.progark.Server.Model.Game;

import java.util.HashMap;
import java.util.Map;

public class GameModel {

    /*
    private final PlayerModel playerOne;
    private final PlayerModel playerTwo;
     */

    private final Map<String, Integer> submittedAnswers = new HashMap<>();
    private int score = 0;
    private float timeRemaining = 60f;

    private final CategoryData.Category category;

    private String categoryId;

    public GameModel() {
        /*
        this.playerOne = new PlayerModel(user1);
        this.playerTwo = new PlayerModel(user2);
         */
        categoryId = "2";


        this.category = CategoryData.getCategory(categoryId);

        if (this.category == null) {
            throw new IllegalArgumentException("Invalid category ID: " + categoryId);
        }
    }
    /*
    public PlayerModel getPlayerOne() { return playerOne; }
    public PlayerModel getPlayerTwo() { return playerTwo; }
    */

    public boolean hasAlreadySubmitted(String answer) {
        return submittedAnswers.keySet().contains(answer.toLowerCase());
    }

    public boolean submitAnswer(String answer) {
        String normalized = answer.toLowerCase();

        if (hasAlreadySubmitted(normalized)) return false;

        int points = getPoints(normalized);

        submittedAnswers.put(normalized, points);
        score += points;

        return true;
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
