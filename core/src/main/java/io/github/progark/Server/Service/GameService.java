package io.github.progark.Server.Service;

import io.github.progark.Server.Model.Game.LobbyModel;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;
import java.util.Map;
import java.util.HashMap;

// This class should only update the database once the results are final
public class GameService {
    private final DatabaseManager databaseManager;
    private LobbyModel lobbyData;

    public GameService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void fetchGame(String userId, String gameId, DataCallback callback) {
        String gamePath = "games/" + userId + "/" + gameId;
        databaseManager.readData(gamePath, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Invalid game data format"));
                    return;
                }

                Map<?, ?> gameData = (Map<?, ?>) data;
                // Convert the map data to your game model
                // You'll need to implement this conversion based on your GameModel structure
                GameModel gameModel = new GameModel();
                // Set the game model properties from gameData
                callback.onSuccess(gameModel);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void updateGameState(String userId, String gameId, GameModel gameModel, DataCallback callback) {
        String gamePath = "games/" + userId + "/" + gameId;

        Map<String, Object> gameData = new HashMap<>();
        gameData.put("score", gameModel.getScore());
        gameData.put("timeRemaining", gameModel.getTimeRemaining());
        gameData.put("submittedAnswers", gameModel.getSubmittedAnswers());
        // Add other game state data as needed

        try {
            databaseManager.writeData(gamePath, gameData);
            callback.onSuccess("Game state updated successfully");
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    // Function for fetching lobby information asynchronuous.
   /*public void fetchLobbyInfo(String id, DataCallback callback) {
        databaseManager.readData("lobbies/" + id, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof Map) {
                    lobbyData = LobbyModel.fromMap(id, (Map<String, Object>) data);
                    callback.onSuccess(lobbyData);
                } else {
                    callback.onFailure(new Exception("Invalid lobby data format"));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }*/

    public LobbyModel getLobbyData() {
        return lobbyData;
    }
}
