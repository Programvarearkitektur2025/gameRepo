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
    private boolean playerOneTurn;

    private Number playerOnePoints;
    private Number playerTwoPoints;

    private List<RoundModel> games;
    private Number currentRound;


    public static GameModel fromMap(String lobbyCode, Map<String, Object> data) {
        GameModel lobby = new GameModel();

        lobby.setLobbyCode(lobbyCode);
        lobby.setPlayerOne((String) data.get("playerOne"));
        lobby.setPlayerTwo((String) data.get("playerTwo"));

        Object difficultyObj = data.get("difficulty");
        if (difficultyObj instanceof Number) {
            lobby.setDifficulty(((Number) difficultyObj).intValue());
        } else {
            lobby.setDifficulty(1); // Default fallback
        }

        lobby.setStatus((String) data.getOrDefault("status", "waiting"));

        Object roundsObj = data.get("rounds");
        if (roundsObj instanceof Number) {
            lobby.setRounds(((Number) roundsObj).intValue());
        } else {
            lobby.setRounds(3); // Default fallback
        }

        Object multiObj = data.get("multiplayer");
        lobby.setMultiplayer(multiObj instanceof Boolean ? (Boolean) multiObj : false);

        Object p1Points = data.get("playerOnePoints");
        lobby.setPlayerOnePoints(p1Points instanceof Number ? (Number) p1Points : 0);

        Object p2Points = data.get("playerTwoPoints");
        lobby.setPlayerTwoPoints(p2Points instanceof Number ? (Number) p2Points : 0);

        Object currentRound = data.get("currentRound");
        lobby.setCurrentRound(currentRound instanceof Number ? ((Number) currentRound).intValue() : 1);

        // createdAt handling stays the same
        Object createdRaw = data.get("createdAt");
        if (createdRaw instanceof Map) {
            Map<String, Object> tsMap = (Map<String, Object>) createdRaw;
            if (tsMap.containsKey("seconds")) {
                long seconds = ((Number) tsMap.get("seconds")).longValue();
                long nanos = tsMap.containsKey("nanoseconds") ? ((Number) tsMap.get("nanoseconds")).longValue() : 0;
                long millis = (seconds * 1000) + (nanos / 1_000_000);
                lobby.setCreatedAt(new Timestamp(millis));
            }
        } else if (createdRaw instanceof Long) {
            lobby.setCreatedAt(new Timestamp((Long) createdRaw));
        }

        lobby.setGames((List<RoundModel>) data.get("games")); // Still a placeholder

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


    // Function for updating the list of rounds played. It is assumed that the game-variable
    // is supposed to contain the rounds of the entire game and that it is populated throughout the
    // game. The function underneeth is called by roundController whenever a round finishes.
    // Function that takes a roundModel and adds it to the List<GameModel> game variable.
    public void setFinishedRound(RoundModel roundModel){
        games.add(roundModel);
        // UpdateUI(); After updating the list of played rounds, we should update the UI.
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

    public Number getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
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
