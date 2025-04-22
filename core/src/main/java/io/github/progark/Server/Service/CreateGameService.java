package io.github.progark.Server.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;
/*
 * CreateGameService.java
 * This class is responsible for creating and managing game lobbies.
 * It handles the creation of new lobbies, joining existing lobbies,
 * and updating game models in the database.
 * It also generates unique lobby codes for new lobbies.
 * The service interacts with the DatabaseManager to perform operations related to game lobbies.
 * The service provides methods for creating a lobby, joining a lobby,
 * and generating a unique lobby code.
 */
public class CreateGameService {

    private final DatabaseManager databaseManager;

    public CreateGameService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
/*
 * createLobby
 * This method is responsible for creating a new game lobby.
 * It retrieves the current user, creates a lobby with the specified parameters,
 * and updates the lobby model with the created lobby's details.
 * It also generates a unique lobby code for the new lobby.
 * The method takes the username, difficulty level, number of rounds,
 */
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
            lobby.setCurrentRound(1);

            databaseManager.writeData("lobbies/" + lobbyCode, lobby);
            callback.onSuccess(lobby);

        } catch (Exception e) {
            callback.onFailure(e);
        }
    }
/*
 * joinLobby
 * This method is responsible for joining an existing game lobby.
 * It retrieves the lobby data from the database, checks if the lobby is available for joining,
 * and updates the lobby model with the joining player's username.
 * It also converts any maps into proper RoundModels and updates the usernames.
 */
    public void joinLobby(String lobbyCode, String joiningUsername, DataCallback callback) {
        String path = "lobbies/" + lobbyCode;

        databaseManager.readData(path, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Lobby not found or invalid format."));
                    return;
                }

                Map<String, Object> lobbyMap = (Map<String, Object>) data;
                GameModel game = GameModel.fromMap(lobbyCode, lobbyMap);

                if (game.getPlayerTwo() != null && !game.getPlayerTwo().isEmpty()) {
                    callback.onFailure(new Exception("Lobby already has a second player."));
                    return;
                }

                game.setPlayerTwo(joiningUsername);

                // ✅ Convert any maps into proper RoundModels and update usernames
                List<Object> updatedRounds = new ArrayList<>();
                if (game.getGames() != null) {
                    for (Object roundObj : game.getGames()) {
                        if (roundObj instanceof RoundModel) {
                            RoundModel round = (RoundModel) roundObj;
                            round.setPlayerUsernames(game.getPlayerOne(), joiningUsername);
                            updatedRounds.add(round);
                        } else if (roundObj instanceof Map) {
                            RoundModel round = RoundModel.fromMap((Map<String, Object>) roundObj);
                            round.setPlayerUsernames(game.getPlayerOne(), joiningUsername);
                            updatedRounds.add(round);
                        }
                    }
                    game.setGames((List<RoundModel>) (List<?>) updatedRounds); // unchecked cast is safe here
                }

                // 🔁 Save updated GameModel
                databaseManager.writeData(path, game.toMap());
                System.out.println("✅ Updated game with joined player: " + joiningUsername);

                callback.onSuccess(game);
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
