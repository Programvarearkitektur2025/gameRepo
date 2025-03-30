package io.github.progark.Server.Model.Game;

import java.util.Map;

public class QuestionModel {
    private String question;
    private Map<String, Integer> answers;
    private Integer difficulty;

    public QuestionModel(String question, Map<String, Integer> answers, Integer difficulty){
        this.question = question;
        this.answers = answers;
        this.difficulty=difficulty;
    }

    public Map<String, Integer> getAnswer(){
        return answers;
    }
}
