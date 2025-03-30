package io.github.progark.Server.Model.Game;

import java.util.Map;

import io.github.progark.Client.Controllers.GameController;

public class QuestionModel {
    private String question;
    private Map<String, Integer> answers;
    private Integer difficulty;
    private GameController gameController;

    public QuestionModel(GameController gameController){
        this.gameController = gameController;
    }

    public Map<String, Integer> getAnswer(){
        return answers;
    }
}
