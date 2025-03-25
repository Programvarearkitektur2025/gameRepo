package io.github.progark.Client.Model;

public class PlayerModel extends UserModel{
    private final String userName;
    private int playerPoints;
    private int guesses;
    private int correctGuesses;

    public PlayerModel(UserModel user){
        // Line necessary when using inheritence.
        super(user.getUsername(), user.getEmail(), user.getUid());
        this.userName = user.getUsername();
    }

    public void incrementNumberOfGuesses(){
        this.guesses++;
    }
    public void incrementCorrectGuess(){
        this.correctGuesses++;
    }
    public void addPoints(int points){
        this.playerPoints += points;
    }
    public int getPlayerPoints(){
        return playerPoints;
    }
    public int getCorrectGuesses(){
        return correctGuesses;
    }
    public int getNumberOfGuesses(){
        return guesses;
    }
}
