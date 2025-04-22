package io.github.progark.Server.Model.Game;

import java.util.List;
/*
 * HomeModel.java
 * This class represents the home model of the application.
 * It contains two lists of game entries: yourTurnGames and theirTurnGames.
 * Each game entry contains information about the game ID, opponent name, opponent avatar, and status.  
 * The class provides methods to get and set these lists.
 * The HomeModel class is used to manage the game entries displayed on the home screen of the application.
 * It is designed to be used in conjunction with the HomeController and HomeView classes.
 * The class is part of the server-side logic and is responsible for handling game data.
 * It is used to display the games that are currently in progress and allow users to navigate to them.
 */
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
