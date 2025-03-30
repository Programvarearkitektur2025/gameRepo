package io.github.progark.Server.Model.Game;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class GameModel {

    private String lobbyCode;
    private String playerOne;
    private String playerTwo;
    private int difficulty;
    private String status;
    private Timestamp createdAt;
    private int rounds;
    private boolean multiplayer;

    private Number playerOnePoints;
    private Number playerTwoPoints;

    private List<RoundModel> games;
  
    public static GameModel fromMap(String lobbyCode, Map<String, Object> data) {
        GameModel lobby = new GameModel();

        lobby.setLobbyCode(lobbyCode);
        lobby.setPlayerOne((String) data.get("playerOne"));
        lobby.setPlayerTwo((String) data.get("playerTwo"));
        lobby.setDifficulty(((Number) data.get("difficulty")).intValue());
        lobby.setStatus((String) data.get("status"));
        lobby.setRounds(((Number) data.get("rounds")).intValue());
        lobby.setMultiplayer((Boolean) data.get("multiplayer"));
        lobby.setPlayerOnePoints(((Number) data.get("playerOnePoints")).intValue());
        lobby.setPlayerTwoPoints(((Number) data.get("playerTwoPoints")).intValue());

        // Handle createdAt as Map or Long
        Object createdRaw = data.get("createdAt");
        if (createdRaw instanceof Map) {
            Map<String, Object> tsMap = (Map<String, Object>) createdRaw;

            if (tsMap.containsKey("seconds")) {
                long seconds = ((Number) tsMap.get("seconds")).longValue();
                long nanos = tsMap.containsKey("nanoseconds")
                    ? ((Number) tsMap.get("nanoseconds")).longValue()
                    : 0;

                long millis = (seconds * 1000) + (nanos / 1_000_000);
                lobby.setCreatedAt(new Timestamp(millis));
            }
        } else if (createdRaw instanceof Long) {
            // Fallback: if createdAt is just a long millis
            lobby.setCreatedAt(new Timestamp((Long) createdRaw));
        }

        // Placeholder for games (deserialize later if needed)
        lobby.setGames((List) data.get("games"));

        return lobby;
    }

    public boolean isFull() {
        return "full".equalsIgnoreCase(status);
    }

    public String getOpponent(String myUsername) {
        if (myUsername.equals(playerOne)) return playerTwo;
        if (myUsername.equals(playerTwo)) return playerOne;
        return null;
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getRounds() {
        return rounds;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public Number getPlayerOnePoints() {
        return playerOnePoints;
    }

    public Number getPlayerTwoPoints() {
        return playerTwoPoints;
    }

    public List<RoundModel> getGames() {
        return games;
    }

    public void setLobbyCode(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public void setPlayerOnePoints(Number playerOnePoints) {
        this.playerOnePoints = playerOnePoints;
    }

    public void setPlayerTwoPoints(Number playerTwoPoints) {
        this.playerTwoPoints = playerTwoPoints;
    }

    public void setGames(List<RoundModel> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "LobbyModel{" +
            "lobbyCode='" + lobbyCode + '\'' +
            ", playerOne='" + playerOne + '\'' +
            ", playerTwo='" + playerTwo + '\'' +
            ", status='" + status + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
