package io.github.progark.Server.Service;

import io.github.progark.Server.Model.Game.HomeModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomeService {
    private final DatabaseManager databaseManager;
    private final Random random;

    public HomeService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.random = new Random();
    }

    private String generateGameId() {
        return String.format("%08d", random.nextInt(100000000));
    }

    public void loadUserGames(String userId, DataCallback callback) {
        String gamesPath = "games/";
        databaseManager.readData(gamesPath, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Database result is not a Map"));
                    return;
                }

                Map<?, ?> gamesMap = (Map<?, ?>) data;
                HomeModel homeModel = new HomeModel();
                List<HomeModel.GameEntry> yourTurnGames = new ArrayList<>();
                List<HomeModel.GameEntry> theirTurnGames = new ArrayList<>();

                // Process games data
                for (Map.Entry<?, ?> entry : gamesMap.entrySet()) {
                    String gameId = (String) entry.getKey();
                    Map<?, ?> gameData = (Map<?, ?>) entry.getValue();

                    String opponentName = (String) gameData.get("opponentName");
                    String opponentAvatar = (String) gameData.get("opponentAvatar");
                    String status = (String) gameData.get("status");
                    boolean isYourTurn = (Boolean) gameData.get("isYourTurn");

                    HomeModel.GameEntry gameEntry = new HomeModel.GameEntry(
                        gameId, opponentName, opponentAvatar, status
                    );

                    if (isYourTurn) {
                        yourTurnGames.add(gameEntry);
                    } else {
                        theirTurnGames.add(gameEntry);
                    }
                }

                homeModel.setYourTurnGames(yourTurnGames);
                homeModel.setTheirTurnGames(theirTurnGames);
                callback.onSuccess(homeModel);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void createNewGame(String userId, String opponentId, DataCallback callback) {
        String gameId = generateGameId();
        String gamePath = "games/" + userId + "/" + gameId;

        Map<String, Object> gameData = Map.of(
            "gameId", gameId,
            "opponentId", opponentId,
            "opponentName", "Opponent", // This should be fetched from user data
            "opponentAvatar", "default_avatar.png",
            "status", "active",
            "isYourTurn", true,
            "createdAt", System.currentTimeMillis()
        );

        try {
            databaseManager.writeData(gamePath, gameData);
            callback.onSuccess(gameId);
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public void joinGame(String userId, String gameId, DataCallback callback) {
        String gamePath = "games/" + userId + "/" + gameId;

        Map<String, Object> gameData = Map.of(
            "gameId", gameId,
            "opponentId", userId,
            "opponentName", "Opponent", // This should be fetched from user data
            "opponentAvatar", "default_avatar.png",
            "status", "active",
            "isYourTurn", false,
            "joinedAt", System.currentTimeMillis()
        );

        try {
            databaseManager.writeData(gamePath, gameData);
            callback.onSuccess(gameId);
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }
}
