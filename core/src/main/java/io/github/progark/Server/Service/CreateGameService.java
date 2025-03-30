package io.github.progark.Server.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import io.github.progark.Server.Model.Game.LobbyModel;
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

            LobbyModel lobby = new LobbyModel();
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
            lobby.setGames(new ArrayList<>());


            // Save lobby to database
            databaseManager.writeData(lobbyCode, lobby);

            // Return the full lobby object to the controller
            callback.onSuccess(lobby);

        } catch (Exception e) {
            callback.onFailure(e);
        }
    }


    private String generateLobbyCode() {
        Random rand = new Random();

        return String.format("%06d", rand.nextInt(1000000));
    }
}
