package io.github.progark.Server.Service;

import java.util.HashMap;
import java.util.Map;

import io.github.progark.Server.Model.Game.LeaderboardModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class LeaderboardService {

    private final DatabaseManager databaseManager;
    private final AuthService authService;
    private static final String LEADERBOARD_DOC = "leaderboard/Leaderboard - 2025";

    public LeaderboardService(DatabaseManager databaseManager, AuthService authService) {
        this.databaseManager = databaseManager;
        this.authService = authService;
    }


    public void loadLeaderboard(DataCallback callback) {
        databaseManager.readData(LEADERBOARD_DOC, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Malformed leaderboard data (not a Map)."));
                    return;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> rawMap = (Map<String, Object>) data;
                Map<String, Integer> nameScoreMap = new HashMap<>();

                for (String userId : rawMap.keySet()) {
                    Object val = rawMap.get(userId);
                    if (val instanceof Number) {
                        int score = ((Number) val).intValue();

                        String username = authService.getUsernameFromUserId(userId);
                        nameScoreMap.put(username, score);
                    }
                }

                LeaderboardModel leaderboard = new LeaderboardModel(nameScoreMap);
                callback.onSuccess(leaderboard);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    /**
     * Øker scoren til en gitt userId med +1 i databasen.
     */
    public void incrementUserScore(String userId, DataCallback callback) {
        databaseManager.readData(LEADERBOARD_DOC, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                Map<String, Object> scoreboard;
                if (!(data instanceof Map)) {
                    scoreboard = new HashMap<>();
                } else {
                    scoreboard = (Map<String, Object>) data;
                }

                int currentScore = 0;
                if (scoreboard.containsKey(userId)) {
                    Object val = scoreboard.get(userId);
                    if (val instanceof Number) {
                        currentScore = ((Number) val).intValue();
                    }
                }

                int newScore = currentScore + 1;
                scoreboard.put(userId, newScore);

                // Skriv til "leaderboard/Leaderboard-2025"
                databaseManager.writeData(LEADERBOARD_DOC, scoreboard);
                callback.onSuccess("Score incremented for " + userId + ", new score = " + newScore);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }
}
