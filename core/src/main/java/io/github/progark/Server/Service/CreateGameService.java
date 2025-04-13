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
            lobby.setDifficulty(1);
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
