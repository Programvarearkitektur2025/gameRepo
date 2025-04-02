package io.github.progark.Server.Model.Game;

import java.util.Map;

import io.github.progark.Client.Controllers.RoundController;

public class QuestionModel {
    private String question;
    private Map<String, Integer> answers;
    private Integer difficulty;
    private RoundController gameController;

    public QuestionModel(RoundController gameController){
        this.gameController = gameController;
    }

    public Map<String, Integer> getAnswer(){
        return answers;
    }
}
