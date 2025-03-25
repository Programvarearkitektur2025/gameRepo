package io.github.progark.Client.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class ResultService {
    private final DatabaseManager databaseManager;

    public ResultService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void saveResult(String gameId,
                           String winner,
                           String loser,
                           int winnerScore,
                           int loserScore,
                           List<String> questions,
                           int numberOfGuessesP1,
                           int numberOfGuessesP2,
                           int correctGuessesP1,
                           int correctGuessesP2,
                           List<String> guessesP1,
                           List<String> guessesP2,
                           DataCallback callback) {

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("winner", winner);
        resultData.put("loser", loser);
        resultData.put("winnerScore", winnerScore);
        resultData.put("loserScore", loserScore);
        resultData.put("finishedAt", new Timestamp(System.currentTimeMillis()).getTime());

        resultData.put("questions", questions);
        resultData.put("numberOfGuessesP1", numberOfGuessesP1);
        resultData.put("numberOfGuessesP2", numberOfGuessesP2);
        resultData.put("correctGuessesP1", correctGuessesP1);
        resultData.put("correctGuessesP2", correctGuessesP2);
        resultData.put("guessesP1", guessesP1);
        resultData.put("guessesP2", guessesP2);

        try {
            databaseManager.writeData("results/" + gameId, resultData);
            callback.onSuccess("Result saved successfully for gameId: " + gameId);
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public void loadResult(String gameId, DataCallback callback) {
        databaseManager.readData("results/" + gameId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }
}

