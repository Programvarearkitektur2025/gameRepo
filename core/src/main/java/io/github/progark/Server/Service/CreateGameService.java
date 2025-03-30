package io.github.progark.Server.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class CreateGameService {

    private final DatabaseManager databaseManager;

    public CreateGameService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createLobby(String username, int difficulty, int rounds, boolean multiplayer, DataCallback callback) {
        try {
            String lobbyCode = generateLobbyCode();

            GameModel lobby = new GameModel();
            lobby.setLobbyCode(lobbyCode);
            lobby.setPlayerOne(username);
            lobby.setPlayerTwo(null);
            lobby.setDifficulty(difficulty);
            lobby.setStatus(multiplayer ? "waiting" : "started");
            lobby.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            lobby.setRounds(rounds);
            lobby.setMultiplayer(multiplayer);
            lobby.setPlayerOnePoints(0);
            lobby.setPlayerTwoPoints(0);
            lobby.setGames(new java.util.ArrayList<>());

            databaseManager.writeData("lobbies/" + lobbyCode, lobby);
            callback.onSuccess(lobby);

        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public void joinLobby(String lobbyCode, String username, DataCallback callback) {
        String path = "lobbies/" + lobbyCode;

        databaseManager.readData(path, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                if (!(result instanceof Map)) {
                    callback.onFailure(new Exception("Invalid lobby format"));
                    return;
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> dataMap = (Map<String, Object>) result;
                GameModel lobby = GameModel.fromMap(lobbyCode, dataMap);

                if (lobby.isFull() || "started".equalsIgnoreCase(lobby.getStatus())) {
                    callback.onFailure(new Exception("Lobby is full or already started"));
                    return;
                }

                lobby.setPlayerTwo(username);
                lobby.setStatus("started");

                databaseManager.writeData("lobbies/" + lobbyCode, lobby);
                callback.onSuccess(lobby);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private String generateLobbyCode() {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000));
    }
}
