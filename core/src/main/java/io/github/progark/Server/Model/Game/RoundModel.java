package io.github.progark.Server.Model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * RoundModel.java
 * This class represents a round in the game.
 * It contains the question, player usernames, their answers, scores, and time remaining.
 * The class provides methods to submit answers, check if players have completed the round, and manage the game state.
 * The RoundModel class is used to manage the game rounds and is typically used in conjunction with the RoundController and RoundView classes.
 * It is part of the server-side logic and is responsible for handling game data related to the rounds.
 */
public class RoundModel {

    private final Map<String, Integer> playerOneAnswers = new HashMap<>();
    private final Map<String, Integer> playerTwoAnswers = new HashMap<>();
    public String playerOneUsername;
    public String playerTwoUsername;

    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private float timeRemaining = 30f;

    private final QuestionModel question;

    private List<String> hasPlayedList = new ArrayList<>();

    public RoundModel(QuestionModel question, String playerOneUsername, String playerTwoUsername) {
        this.question = question;

        if (question == null) {
            throw new IllegalArgumentException("Invalid question.");
        }

        this.playerOneUsername = playerOneUsername;
        this.playerTwoUsername = playerTwoUsername;
    }


    private Map<String, Integer> getAnswerMapForPlayer(String username) {
        if (username.equals(playerOneUsername)) return playerOneAnswers;
        return playerTwoAnswers;
    }

    public boolean hasAlreadySubmitted(String username, String answer) {
        String normalized = capitalizeFirstLetter(answer.trim());
        return getAnswerMapForPlayer(username).containsKey(normalized);
    }


    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    public boolean submitAnswer(String username, String answer) {
        String normalized = capitalizeFirstLetter(answer.trim());

        if (hasAlreadySubmitted(username, normalized)) return false;

        int points = getPoints(normalized);
        if (username.equals(playerOneUsername)) {
            playerOneAnswers.put(normalized, points);
            playerOneScore += points;
        } else {
            playerTwoAnswers.put(normalized, points);
            playerTwoScore += points;
        }

        markPlayerCompleted(username);
        return true;
    }

    public void setPlayerUsernames(String playerOne, String playerTwo) {
        this.playerOneUsername = playerOne;
        this.playerTwoUsername = playerTwo;
    }


    private Integer getPoints(String answer) {
        Map<String, Integer> validAnswers = question.getAnswer();
        String normalized = capitalizeFirstLetter(answer.trim());

        Object value = validAnswers.get(normalized);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
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

    public static RoundModel fromMap(Map<String, Object> map) {
        if (map == null) return null;

        String questionText = (String) map.get("question");

        Map<String, Integer> answers = new HashMap<>();
        Object answersObj = map.get("allValidAnswers");
        if (answersObj instanceof Map) {
            Map<?, ?> raw = (Map<?, ?>) answersObj;
            for (Map.Entry<?, ?> entry : raw.entrySet()) {
                Object val = entry.getValue();
                int score = val instanceof Number ? ((Number) val).intValue() : 0;
                answers.put(String.valueOf(entry.getKey()), score);
            }
        }

        int difficulty = map.get("difficulty") instanceof Number ? ((Number) map.get("difficulty")).intValue() : 1;
        QuestionModel question = new QuestionModel(questionText, answers, difficulty);

        if (question.getAnswer() == null) {
            throw new IllegalArgumentException("Invalid question or missing answers.");
        }

        RoundModel round = new RoundModel(question, null, null);

        if (map.get("playerOneUsername") instanceof String) {
            round.playerOneUsername = (String) map.get("playerOneUsername");
        }
        if (map.get("playerTwoUsername") instanceof String) {
            round.playerTwoUsername = (String) map.get("playerTwoUsername");
        }

        Object p1Ans = map.get("playerOneAnswers");
        if (p1Ans instanceof Map) {
            Map<?, ?> raw = (Map<?, ?>) p1Ans;
            for (Map.Entry<?, ?> entry : raw.entrySet()) {
                Object val = entry.getValue();
                int score = val instanceof Number ? ((Number) val).intValue() : 0;
                round.playerOneAnswers.put(String.valueOf(entry.getKey()), score);
            }
        }

        Object p2Ans = map.get("playerTwoAnswers");
        if (p2Ans instanceof Map) {
            Map<?, ?> raw = (Map<?, ?>) p2Ans;
            for (Map.Entry<?, ?> entry : raw.entrySet()) {
                Object val = entry.getValue();
                int score = val instanceof Number ? ((Number) val).intValue() : 0;
                round.playerTwoAnswers.put(String.valueOf(entry.getKey()), score);
            }
        }

        Object p1Score = map.get("playerOneScore");
        if (p1Score instanceof Number) {
            round.setPlayerOneScore(((Number) p1Score).intValue());
        }

        Object p2Score = map.get("playerTwoScore");
        if (p2Score instanceof Number) {
            round.setPlayerTwoScore(((Number) p2Score).intValue());
        }

        Object timeLeft = map.get("timeRemaining");
        if (timeLeft instanceof Number) {
            round.timeRemaining = ((Number) timeLeft).floatValue();
        }

        return round;
    }

    public boolean hasPlayerCompleted(String username) {
        boolean amIPlayerOne = username.equals(playerOneUsername);
        if(amIPlayerOne){
            return !playerOneAnswers.isEmpty();
        }else{
            return !playerTwoAnswers.isEmpty();
        }
    }

    public void markPlayerCompleted(String username) {
        if (!hasPlayedList.contains(username)) {
            hasPlayedList.add(username);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("question", question.getQuestion());
        map.put("allValidAnswers", question.getAnswer());
        map.put("playerOneUsername", playerOneUsername);
        map.put("playerTwoUsername", playerTwoUsername);
        map.put("playerOneAnswers", playerOneAnswers);
        map.put("playerTwoAnswers", playerTwoAnswers);
        map.put("playerOneScore", playerOneScore);
        map.put("playerTwoScore", playerTwoScore);
        map.put("timeRemaining", timeRemaining);
        map.put("timeUp", isTimeUp());
        return map;
    }

    public void setTimeRemaining(float number){
        this.timeRemaining = number;
    }

    public boolean hasBothPlayersAnswered(){
        return !playerOneAnswers.isEmpty() && !playerTwoAnswers.isEmpty();
    }
}
