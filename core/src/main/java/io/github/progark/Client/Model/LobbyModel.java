package io.github.progark.Client.Model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class LobbyModel {

    private String lobbyCode;
    private String playerOne;
    private String playerTwo;
    private String status;
    private Timestamp createdAt;

    public LobbyModel(String lobbyCode, String playerOne, String playerTwo, String status, Timestamp createdAt) {
        this.lobbyCode = lobbyCode;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static LobbyModel fromMap(String lobbyCode, Map<String, Object> data) {
        String playerOne = (String) data.get("playerOne");
        String playerTwo = data.get("playerTwo") != null ? (String) data.get("playerTwo") : null;
        String status = (String) data.get("status");

        Timestamp createdAt = null;
        Object createdRaw = data.get("createdAt");

        if (createdRaw instanceof Timestamp) {
            createdAt = (Timestamp) createdRaw;
        } else if (createdRaw instanceof Long) {
            createdAt = new Timestamp((Long) createdRaw);
        } else if (createdRaw instanceof Double) {
            createdAt = new Timestamp(((Double) createdRaw).longValue());
        }

        return new LobbyModel(lobbyCode, playerOne, playerTwo, status, createdAt);
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

    public Date getCreatedAtAsDate() {
        return new Date(createdAt.getTime());
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
}
