package io.github.progark.Server.Model.Game;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultModel {
    private List<String> gameIds;
    private List<GameModel> games;
    private int scoreP1;
    private int scoreP2;
    private List<String> categories;
    private Map<String, Integer> guessesP1;
    private Map<String, Integer> guessesP2;

}

