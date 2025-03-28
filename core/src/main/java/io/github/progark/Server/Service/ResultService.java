package io.github.progark.Server.Service;

import io.github.progark.Server.Model.Game.ResultModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultService {
    private final DatabaseManager databaseManager;

    public ResultService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }


    public void saveResult(ResultModel model, DataCallback callback) {
        String key = "results/" + model.getGameId();


        Map<String, Object> data = new HashMap<>();
        data.put("gameId", model.getGameId());
        data.put("winner", model.getWinner());
        data.put("loser", model.getLoser());
        data.put("winnerScore", model.getWinnerScore());
        data.put("loserScore", model.getLoserScore());
        data.put("finishedAt", model.getFinishedAt() != null ? model.getFinishedAt().getTime() : null);
        data.put("questions", model.getQuestions());
        data.put("numberOfGuessesP1", model.getNumberOfGuessesP1());
        data.put("numberOfGuessesP2", model.getNumberOfGuessesP2());
        data.put("correctGuessesP1", model.getCorrectGuessesP1());
        data.put("correctGuessesP2", model.getCorrectGuessesP2());
        data.put("guessesP1", model.getGuessesP1());
        data.put("guessesP2", model.getGuessesP2());

        try {

            databaseManager.writeData(key, data);
            callback.onSuccess("Result saved for " + model.getGameId());
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }


    public void loadResult(String gameId, DataCallback callback) {
        String key = "results/" + gameId;
        databaseManager.readData(key, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {

                    callback.onFailure(new Exception("Database result is not a Map"));
                    return;
                }

                Map<?, ?> rawMap = (Map<?, ?>) data;

                ResultModel model = new ResultModel();


                model.setGameId((String) rawMap.get("gameId"));
                model.setWinner((String) rawMap.get("winner"));
                model.setLoser((String) rawMap.get("loser"));


                model.setWinnerScore(castToInt(rawMap.get("winnerScore")));
                model.setLoserScore(castToInt(rawMap.get("loserScore")));


                Long finishedAtLong = castToLong(rawMap.get("finishedAt"));
                if (finishedAtLong != null) {
                    model.setFinishedAt(new Timestamp(finishedAtLong));
                }


                model.setQuestions(castToStringList(rawMap.get("questions")));
                model.setGuessesP1(castToStringList(rawMap.get("guessesP1")));
                model.setGuessesP2(castToStringList(rawMap.get("guessesP2")));

                model.setNumberOfGuessesP1(castToInt(rawMap.get("numberOfGuessesP1")));
                model.setNumberOfGuessesP2(castToInt(rawMap.get("numberOfGuessesP2")));
                model.setCorrectGuessesP1(castToInt(rawMap.get("correctGuessesP1")));
                model.setCorrectGuessesP2(castToInt(rawMap.get("correctGuessesP2")));


                callback.onSuccess(model);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }


    private int castToInt(Object val) {
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        return 0;
    }

    private Long castToLong(Object val) {
        if (val instanceof Number) {
            return ((Number) val).longValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<String> castToStringList(Object val) {
        if (val instanceof List) {
            return (List<String>) val;
        }
        return null;
    }
}
