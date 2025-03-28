package io.github.progark.Server.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class LobbyService {

    private final DatabaseManager databaseManager;

    public LobbyService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createLobby(String playerOne, DataCallback callback) {
        String generatedCode = generateLobbyCode();


        Map<String, Object> lobbyData = new HashMap<>();
        lobbyData.put("playerOne", playerOne);
        lobbyData.put("playerTwo", null);
        lobbyData.put("status", "waiting");
        lobbyData.put("createdAt", new Timestamp(System.currentTimeMillis()));

        try {
            databaseManager.writeData("lobbies/" + generatedCode, lobbyData);
            callback.onSuccess(generatedCode);
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public void joinLobby(String lobbyCode, String guestUsername, DataCallback dataCallback) {

        databaseManager.readData("lobbies/" + lobbyCode, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    dataCallback.onFailure(new Exception("Malformed lobby data"));
                    return;
                }

                Map<String, Object> lobby = (Map<String, Object>) data;

                Object playerTwo = lobby.get("playerTwo");
                if (playerTwo == null || playerTwo.toString().isEmpty()) {
                    lobby.put("playerTwo", playerTwo);
                    lobby.put("status", "full");

                    try {
                        databaseManager.writeData("lobbies/" + lobbyCode, lobby);
                        dataCallback.onSuccess(lobbyCode);
                    } catch (Exception e) {
                        dataCallback.onFailure(e);
                    }
                } else {
                    dataCallback.onFailure(new Exception("Lobby already full"));
                }


            }

            @Override
            public void onFailure(Exception e) {
                dataCallback.onFailure(e);
            }

        });
    }


    private String generateLobbyCode() {
        Random rand = new Random();

        return String.format("%06d", rand.nextInt(1000000));
    }
}
