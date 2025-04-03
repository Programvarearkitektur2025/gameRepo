package io.github.progark.Server.Model.Game;

import java.util.Map;

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
