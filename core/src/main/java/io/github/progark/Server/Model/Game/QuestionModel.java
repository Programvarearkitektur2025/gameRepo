package io.github.progark.Server.Model.Game;

import java.util.Map;

import io.github.progark.Client.Controllers.RoundController;

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
}
