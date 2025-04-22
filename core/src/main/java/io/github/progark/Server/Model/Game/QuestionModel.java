package io.github.progark.Server.Model.Game;

import java.util.HashMap;
import java.util.Map;

import io.github.progark.Client.Controllers.RoundController;
/*
 * QuestionModel.java
 * This class represents a question in the game.
 * It contains the question text, possible answers, and difficulty level.
 * The class provides methods to get and set the question, answers, and difficulty level.
 * It also includes a static method to create a QuestionModel instance from a map of data.
 * The QuestionModel class is used to manage the questions in the game and is typically used in conjunction with the QuestionController and QuestionView classes.
 */
public class QuestionModel {
    public String question;
    private Map<String, Integer> answers;
    private Integer difficulty;
    private RoundController gameController;

    public QuestionModel(String question, Map<String, Integer> answers, Integer difficulty) {
        this.question = question;
        this.answers = answers;
        this.difficulty = difficulty;
    }

    public Map<String, Integer> getAnswer() {
        return answers;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Integer> answers) {
        this.answers = answers;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public static QuestionModel fromMap(Map<String, Object> map) {
        if (map == null) return null;

        String questionText = (String) map.get("Question");
        Integer difficulty = null;

        Object diffObj = map.get("Difficulty");
        if (diffObj instanceof Number) {
            difficulty = ((Number) diffObj).intValue();
        }

        Map<String, Integer> answers = new HashMap<>();
        Object answersObj = map.get("Answers");
        if (answersObj instanceof Map) {
            Map<?, ?> raw = (Map<?, ?>) answersObj;
            for (Map.Entry<?, ?> entry : raw.entrySet()) {
                String key = capitalizeFirstLetter(entry.getKey().toString());

                Object value = entry.getValue();
                if (value instanceof Number) {
                    answers.put(key, ((Number) value).intValue()); // ✅ Safe conversion
                }
            }
        }

        return new QuestionModel(questionText, answers, difficulty);
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }



}
