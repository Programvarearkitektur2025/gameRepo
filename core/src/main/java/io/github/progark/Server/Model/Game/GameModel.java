package io.github.progark.Server.Model.Game;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * GameModel.java
 * This class represents the game model in the application.
 * It contains information about the game lobby, players, rounds, and game status.
 * The class provides methods to create a game model from a map, check if the game is full,
 * get opponent information, and convert the game model to a map for storage.
 * It also includes methods to manage game rounds and player points.
 * The class is designed to be used in conjunction with the GameService and DatabaseManager classes.
 * The GameModel class is part of the server-side logic and is used to manage game data.
 * It is responsible for handling the game state, player interactions, and synchronization with the database.
 */
public class GameModel {

    private String lobbyCode;
    private String playerOne;
    private String playerTwo;
    private int difficulty;
    private String status;
    private Timestamp createdAt;
    private int rounds;
    private boolean leaderboardUpdated = false;
    private boolean multiplayer;

    private Number playerOnePoints;
    private Number playerTwoPoints;

    private List<RoundModel> games;
    private Number currentRound;

/*
 * fromMap
 * This method creates a GameModel instance from a map of data.
 * It extracts the relevant fields from the map and sets them in the GameModel instance.
 * The method handles different data types and provides default values for missing fields.
 * It also converts timestamps and rounds into the appropriate format.
 * The method is designed to be used when retrieving game data from the database.
 * It ensures that the GameModel instance is populated with the correct data for further processing.
 * The method is static and can be called without creating an instance of the GameModel class.
 */
    public static GameModel fromMap(String lobbyCode, Map<String, Object> data) {
        GameModel lobby = new GameModel();

        lobby.setLobbyCode(lobbyCode);
        lobby.setPlayerOne((String) data.get("playerOne"));
        lobby.setPlayerTwo((String) data.get("playerTwo"));

        Object difficultyObj = data.get("difficulty");
        if (difficultyObj instanceof Number) {
            lobby.setDifficulty(((Number) difficultyObj).intValue());
        } else {
            lobby.setDifficulty(1);
        }

        lobby.setStatus((String) data.getOrDefault("status", "waiting"));

        Object roundsObj = data.get("rounds");
        if (roundsObj instanceof Number) {
            lobby.setRounds(((Number) roundsObj).intValue());
        } else {
            lobby.setRounds(3);
        }

        Object multiObj = data.get("multiplayer");
        lobby.setMultiplayer(multiObj instanceof Boolean ? (Boolean) multiObj : false);

        Object p1Points = data.get("playerOnePoints");
        lobby.setPlayerOnePoints(p1Points instanceof Number ? (Number) p1Points : 0);

        Object p2Points = data.get("playerTwoPoints");
        lobby.setPlayerTwoPoints(p2Points instanceof Number ? (Number) p2Points : 0);

        Object currentRound = data.get("currentRound");
        lobby.setCurrentRound(currentRound instanceof Number ? ((Number) currentRound).intValue() : 1);

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

        Object gamesObj = data.get("games");
        if (gamesObj instanceof List<?>) {
            List<?> rawGames = (List<?>) gamesObj;
            List<RoundModel> rounds = new ArrayList<>();

            for (Object obj : rawGames) {
                if (obj instanceof Map) {
                    rounds.add(RoundModel.fromMap((Map<String, Object>) obj));
                }
            }

            lobby.setGames(rounds);
        } else {
            lobby.setGames(new ArrayList<>());
        }


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

    public boolean isLeaderboardUpdated() {
        return leaderboardUpdated;
    }

    public void setLeaderboardUpdated(boolean leaderboardUpdated) {
        this.leaderboardUpdated = leaderboardUpdated;
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

    public Number getCurrentRound() {
        if (multiplayer){
            List<RoundModel> games = getGames();
            int roundIndex=0;
            for (RoundModel round : games){
                if (round.hasBothPlayersAnswered()) roundIndex++;
            }
            return roundIndex;
        }else return currentRound;
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("lobbyCode", lobbyCode);
        map.put("playerOne", playerOne);
        map.put("playerTwo", playerTwo);
        map.put("difficulty", difficulty);
        map.put("status", status);
        map.put("rounds", rounds);
        map.put("multiplayer", multiplayer);
        map.put("playerOnePoints", playerOnePoints != null ? playerOnePoints : 0);
        map.put("playerTwoPoints", playerTwoPoints != null ? playerTwoPoints : 0);
        map.put("currentRound", currentRound != null ? currentRound : 1);

        if (createdAt != null) {
            Map<String, Object> ts = new HashMap<>();
            ts.put("seconds", createdAt.getTime() / 1000);
            ts.put("nanoseconds", (createdAt.getTime() % 1000) * 1_000_000);
            map.put("createdAt", ts);
        }

        if (games != null && !games.isEmpty()) {
            List<Map<String, Object>> roundMaps = new ArrayList<>();
            for (Object roundObj : games) {
                if (roundObj instanceof RoundModel) {
                    roundMaps.add(((RoundModel) roundObj).toMap());
                } else if (roundObj instanceof Map) {
                    // Already stored as a map — no need to reprocess
                    roundMaps.add((Map<String, Object>) roundObj);
                }
            }
            map.put("games", roundMaps);
        }
        return map;
    }

    public void setActiveRound(Number index){
        this.currentRound = (Number) ((int) index );
    }
}
