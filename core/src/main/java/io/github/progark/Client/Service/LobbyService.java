package io.github.progark.Client.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import io.github.progark.Client.Model.LobbyModel;
import io.github.progark.Client.Model.UserModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class LobbyService {

    private final DatabaseManager databaseManager;
    private final AuthService authService;
    private final Json json = new Json();

    public LobbyService(DatabaseManager databaseManager, AuthService authService) {
        this.databaseManager = databaseManager;
        this.authService = authService;
    }

    public void createLobby(String playerUsername, DataCallback callback) {
        String generatedCode = generateLobbyCode();

        LobbyModel lobby = new LobbyModel(
            generatedCode,  
            playerUsername,
            null,
            "waiting",
            new Timestamp(System.currentTimeMillis())
        );


        try {
            databaseManager.writeData("lobbies/" + generatedCode, lobbyModel);
            callback.onSuccess(generatedCode);
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }


    public void joinLobby(String lobbyCode, String playerTwoUsername, DataCallback callback) {
        databaseManager.readData(lobbyCode, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof String)) {
                    callback.onFailure(new Exception("Invalid lobby data format"));
                    return;
                }

                try {
                    LobbyModel lobby = json.fromJson(LobbyModel.class, (String) data);

                    if (lobby.getPlayerTwo() == null || lobby.getPlayerTwo().isEmpty()) {
                        lobby.setPlayerTwo(playerTwoUsername);
                        lobby.setStatus("full");

                        databaseManager.writeData("lobbies/" + generatedCode, lobby);
                        callback.onSuccess(lobby);
                    } else {
                        callback.onFailure(new Exception("Lobby already full"));
                    }
                } catch (Exception e) {
                    callback.onFailure(new Exception("Failed to parse lobby data: " + e.getMessage()));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }


    public void subscribeToLobbyUpdates(String lobbyCode, DataCallback callback) {
        databaseManager.subscribeToDocument(lobbyCode, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof String)) {
                    callback.onFailure(new Exception("Invalid lobby data format"));
                    return;
                }

                try {
                    LobbyModel updatedLobby = json.fromJson(LobbyModel.class, (String) data);
                    callback.onSuccess(updatedLobby);
                } catch (Exception e) {
                    callback.onFailure(e);
                }
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
