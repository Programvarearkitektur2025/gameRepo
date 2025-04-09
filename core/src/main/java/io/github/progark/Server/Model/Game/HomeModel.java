package io.github.progark.Server.Model.Game;

import java.util.List;

public class HomeModel {
    private List<GameEntry> yourTurnGames;
    private List<GameEntry> theirTurnGames;

    public HomeModel() {
        this.yourTurnGames = new java.util.ArrayList<>();
        this.theirTurnGames = new java.util.ArrayList<>();
    }

    public List<GameEntry> getYourTurnGames() {
        return yourTurnGames;
    }

    public void setYourTurnGames(List<GameEntry> yourTurnGames) {
        this.yourTurnGames = yourTurnGames;
    }

    public List<GameEntry> getTheirTurnGames() {
        return theirTurnGames;
    }

    public void setTheirTurnGames(List<GameEntry> theirTurnGames) {
        this.theirTurnGames = theirTurnGames;
    }

    public static class GameEntry {
        private String gameId;
        private String opponentName;
        private String opponentAvatar;
        private String status;

        public GameEntry(String gameId, String opponentName, String opponentAvatar) {
            this.gameId = gameId;
            this.opponentName = opponentName;
            this.opponentAvatar = opponentAvatar;
            this.status = status;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getOpponentName() {
            return opponentName;
        }

        public void setOpponentName(String opponentName) {
            this.opponentName = opponentName;
        }

        public String getOpponentAvatar() {
            return opponentAvatar;
        }

        public void setOpponentAvatar(String opponentAvatar) {
            this.opponentAvatar = opponentAvatar;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
