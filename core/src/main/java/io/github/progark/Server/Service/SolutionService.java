package io.github.progark.Server.Service;

import java.util.Map;

import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class SolutionService {
    private final DatabaseManager databaseManager;

    public SolutionService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Get correct answers in given category
    public void getQuestion(String questionID, DataCallback callback) {
        String questionPath = "questions/" + questionID; // Path til collection i firebase
        databaseManager.readData(questionPath, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Invalid game data format"));
                    return;
                }
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });

    }
}
