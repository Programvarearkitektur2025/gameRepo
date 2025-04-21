package io.github.progark.Server.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.LeaderboardModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;
import java.util.LinkedHashMap;



public class LeaderboardService {

    private final DatabaseManager databaseManager;
    private final AuthService authService;
    private static final String LEADERBOARD_DOC = "leaderboard/Leaderboard - 2025";

    public LeaderboardService(DatabaseManager databaseManager, AuthService authService) {
        this.databaseManager = databaseManager;
        this.authService = authService;
    }

    public void getUserLeaderboardScore(String username, DataCallback callback) {
        databaseManager.readData(LEADERBOARD_DOC, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Malformed scoreboard data"));
                    return;
                }
                Map<String,Object> rawMap = (Map<String,Object>) data;
                if (!rawMap.containsKey(username)) {
                    callback.onFailure(new Exception("No scoreboard entry for user: " + username));
                    return;
                }
                Object val = rawMap.get(username);
                int score = (val instanceof Number) ? ((Number) val).intValue() : 0;
                callback.onSuccess(score);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void loadLeaderboard(DataCallback callback) {
        databaseManager.readData(LEADERBOARD_DOC, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Malformed leaderboard data (not a Map)."));
                    return;
                }
                Map<String, Object> rawMap = (Map<String, Object>) data; // "Marcus"->score etc.

                Map<String, Integer> nameScoreMap = new HashMap<>();
                for (Map.Entry<String, Object> e : rawMap.entrySet()) {
                    int score = 0;
                    if (e.getValue() instanceof Number) {
                        score = ((Number) e.getValue()).intValue();
                    }
                    // Her er e.getKey() = "Marcus"
                    nameScoreMap.put(e.getKey(), score);
                }
                // Sorter 10
                Map<String, Integer> sortedTop10 = nameScoreMap.entrySet()
                    .stream()
                    .sorted((a,b)->b.getValue().compareTo(a.getValue()))
                    .limit(10)
                    .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2)->e1,
                        LinkedHashMap::new
                    ));
                callback.onSuccess(new LeaderboardModel(sortedTop10));
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

    public void updateLeaderBoard(GameModel gameModel) {
        String playerOne = gameModel.getPlayerOne();
        String playerTwo = gameModel.getPlayerTwo();
        int playerOnePoints = gameModel.getPlayerOnePoints().intValue();
        int playerTwoPoints = gameModel.getPlayerTwoPoints().intValue();

        // Determine the winner
        String winner;
        int winnerPoints;

        if (playerOnePoints > playerTwoPoints) {
            winner = playerOne;
            winnerPoints = playerOnePoints;
        } else if (playerTwoPoints > playerOnePoints) {
            winner = playerTwo;
            winnerPoints = playerTwoPoints;
        } else {
            // It's a tie, don't update the leaderboard
            return;
        }

        databaseManager.readData(LEADERBOARD_DOC, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                Map<String, Object> leaderboard;

                if (data instanceof Map) {
                    leaderboard = (Map<String, Object>) data;
                } else {
                    leaderboard = new HashMap<>();
                }

                Object existingVal = leaderboard.getOrDefault(winner, 0);
                int existingScore = existingVal instanceof Number ? ((Number) existingVal).intValue() : 0;

                incrementUserScore(winner, new DataCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        System.out.println("🏅 Leaderboard updated for winner: " + winner);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("❌ Failed to increment leaderboard for winner: " + e.getMessage());
                    }
                });


                databaseManager.writeData(LEADERBOARD_DOC, leaderboard);
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Failed to read leaderboard: " + e.getMessage());
            }
        });
    }


}
