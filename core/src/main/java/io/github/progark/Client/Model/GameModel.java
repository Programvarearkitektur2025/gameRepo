package io.github.progark.Client.Model;

import java.sql.Timestamp;

public class GameModel {
    private int round;
    private final PlayerModel playerOne;
    private final PlayerModel playerTwo;

    public GameModel(UserModel user1, UserModel user2){
        this.playerOne = new PlayerModel(user1);
        this.playerTwo = new PlayerModel(user2);
    }

    public PlayerModel getPlayerOne(){
        return playerOne;
    }
    public PlayerModel getPlayerTwo(){
        return playerTwo;
    }
}
