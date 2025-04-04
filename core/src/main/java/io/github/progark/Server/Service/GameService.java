package io.github.progark.Server.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class GameService {

    private final DatabaseManager databaseManager;
    public GameService(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }


    public void getRelevantGames(UserModel user, DataCallback callback) {
        String path = "lobbies";  // Correct path to fetch all game documents
        List<Map<String, GameModel>> relevantGames = new ArrayList<>();

        databaseManager.readData(path, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof List<?>) {
                    List<Map<String, Object>> gameList = (List<Map<String, Object>>) data;
                    List<Map<String, Object>> relevantGames = new ArrayList<>();

                    for (Map<String, Object> gameMap : gameList) {  // Use Object instead of GameModel
                        for (Map.Entry<String, Object> gameData : gameMap.entrySet()) {
                            String gameId = gameData.getKey();
                            GameModel game = GameModel.fromMap(gameId, gameMap);


                                if (game.getPlayerOne().equals(user.getUsername()) || (game.getPlayerTwo() != null && game.getPlayerTwo().equals(user.getUsername()))) {
                                    Map<String, Object> gameEntry = new HashMap<>();
                                    gameEntry.put("gameId", gameId);
                                    gameEntry.put("game", game);
                                    relevantGames.add(gameEntry);
                                }
                        }
                    }
                    callback.onSuccess(relevantGames);
                } else {
                    callback.onFailure(new Exception("Invalid data format"));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

}
