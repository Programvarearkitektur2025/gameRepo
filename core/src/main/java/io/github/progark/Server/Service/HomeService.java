package io.github.progark.Server.Service;

import io.github.progark.Server.Model.Game.HomeModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
/*
 * HomeService.java
 * This class is responsible for managing the home screen functionality.
 * It handles loading user games, creating new games, and retrieving game information.
 * It interacts with the DatabaseManager to perform operations related to game data.
 * The service provides methods for generating unique game IDs, loading user games,
 * creating new games, and retrieving game information by lobby code.
 */
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

    private void getLobbyInformation(DataCallback callback){
        String lobbyPath = "lobbies/";
        databaseManager.readData(lobbyPath, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map))
                {
                    callback.onFailure(new Exception("Database result is not a Map"));
                    return;
                }
                Map<String,Object> lobbyMap = (Map<String,Object>) data;
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getGameByLobbyCode(String lobbyCode, DataCallback callback) {
        databaseManager.readData("lobbies/" + lobbyCode, callback);
    }



    public void loadUserGames(String userId, DataCallback callback) {
/*        String gamesPath = "games/";
        databaseManager.readData(gamesPath, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Database result is not a Map"));
                    return;
                }

                Map<String, Object> gamesMap = (Map<String, Object>) data;
                HomeModel homeModel = new HomeModel();
                List<HomeModel.GameEntry> yourTurnGames = new ArrayList<>();
                List<HomeModel.GameEntry> theirTurnGames = new ArrayList<>();

                // Process games data
                for (Map.Entry<?, ?> entry : gamesMap.entrySet()) {
                    String gameId = (String) entry.getKey();
                    Map<?, ?> gameData = (Map<?, ?>) entry.getValue();

                    // Check if this game is associated with the user
                    String creatorId = (String) gameData.get("creatorId");
                    String opponentId = (String) gameData.get("opponentId");

                    if (!userId.equals(creatorId) && !userId.equals(opponentId)) {
                        continue; // Skip games not associated with this user
                    }

                    String opponentName = (String) gameData.get("opponentName");
                    String opponentAvatar = (String) gameData.get("opponentAvatar");
                    String status = (String) gameData.get("status");
                    boolean isYourTurn = userId.equals(creatorId) ?
                        (Boolean) gameData.get("isCreatorTurn") :
                        !(Boolean) gameData.get("isCreatorTurn");

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

 */
    }
/*
 * createNewGame
 * This method creates a new game in the database.
 * It generates a unique game ID, sets the initial game data,
 * and writes the data to the database.
 */
    public void createNewGame(String userId, String opponentId, DataCallback callback) {
        String gameId = generateGameId();
        String gamePath = "games/" + gameId;

        Map<String, Object> gameData = Map.of(
            "gameId", gameId,
            "creatorId", userId,
            "opponentId", opponentId,
            "opponentName", "Opponent",
            "opponentAvatar", "default_avatar.png",
            "status", "active",
            "isCreatorTurn", true,
            "createdAt", System.currentTimeMillis()
        );

        try {
            databaseManager.writeData(gamePath, gameData);
            callback.onSuccess(gameId);
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

}
