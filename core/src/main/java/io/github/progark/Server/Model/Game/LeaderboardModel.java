package io.github.progark.Server.Model.Game;

import java.util.Map;
/*
 * LeaderboardModel.java
 * This class represents the model for the leaderboard in the game.
 * It contains a map of user IDs to their scores.
 * The class provides methods to get and set the user scores, as well as incrementing the score for a specific user.
 * The LeaderboardModel class is used to manage the leaderboard data and is typically used in conjunction with the LeaderboardController and LeaderboardView classes.
 * It is part of the server-side logic and is responsible for handling game data related to the leaderboard.
 * The class is designed to be used in conjunction with the LeaderboardService and DatabaseManager classes.
 */
public class LeaderboardModel {
    private Map<String, Integer> userScore;
    public LeaderboardModel(){};
    public LeaderboardModel(Map<String,Integer> userScore){
        this.userScore = userScore;
    }
    public Map<String, Integer> getUserScore(){
        return userScore;
    }
    public void setUserScore(Map<String, Integer> userScore){
        this.userScore = userScore;
    }

    public void incrementScore(String userId){
        int currentScore = userScore.getOrDefault(userId, 0);
        userScore.put(userId,currentScore);
    }

}
