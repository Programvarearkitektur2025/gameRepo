package io.github.progark.Server.Model.Game;

import java.sql.Timestamp;
import java.util.List;

public class ResultModel {
    private String gameId;
    private String winner;
    private String loser;
    private int winnerScore;
    private int loserScore;
    private Timestamp finishedAt;
    private List<String> questions;
    private int numberOfGuessesP1;
    private int numberOfGuessesP2;
    private int correctGuessesP1;
    private int correctGuessesP2;

    private List<String> guessesP1;
    private List<String> guessesP2;


    public ResultModel(){}

    public ResultModel(String gameId,
                       String winner,
                       String loser,
                       int winnerScore,
                       int loserScore,
                       Timestamp finishedAt,
                       List<String> questions,
                       int numberOfGuessesP1,
                       int numberOfGuessesP2,
                       int correctGuessesP1,
                       int correctGuessesP2,
                       List<String> guessesP1,
                       List<String> guessesP2) {
        this.gameId = gameId;
        this.winner = winner;
        this.loser = loser;
        this.winnerScore = winnerScore;
        this.loserScore = loserScore;
        this.finishedAt = finishedAt;
        this.questions = questions;
        this.numberOfGuessesP1 = numberOfGuessesP1;
        this.numberOfGuessesP2 = numberOfGuessesP2;
        this.correctGuessesP1 = correctGuessesP1;
        this.correctGuessesP2 = correctGuessesP2;
        this.guessesP1 = guessesP1;
        this.guessesP2 = guessesP2;
    }






    public String getGameId() {return gameId;}
    public void setGameId(String GameId) {this.gameId =gameId;}

    public String getWinner(){return winner;}
    public void setWinner(String winner){this.winner = winner;}

    public String getLoser(){return loser;}
    public void setLoser(String loser){this.loser = loser;}

    public int getWinnerScore() {return winnerScore;}
    public void setWinnerScore(int winnerScore){this.winnerScore = winnerScore;}

    public int getLoserScore() { return loserScore; }
    public void setLoserScore(int loserScore) { this.loserScore = loserScore; }

    public Timestamp getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Timestamp finishedAt) { this.finishedAt = finishedAt; }

    public List<String> getQuestions() { return questions; }
    public void setQuestions(List<String> questions) { this.questions = questions; }

    public int getNumberOfGuessesP1() { return numberOfGuessesP1; }
    public void setNumberOfGuessesP1(int numberOfGuessesP1) { this.numberOfGuessesP1 = numberOfGuessesP1; }

    public int getNumberOfGuessesP2() { return numberOfGuessesP2; }
    public void setNumberOfGuessesP2(int numberOfGuessesP2) { this.numberOfGuessesP2 = numberOfGuessesP2; }

    public int getCorrectGuessesP1() { return correctGuessesP1; }
    public void setCorrectGuessesP1(int correctGuessesP1) { this.correctGuessesP1 = correctGuessesP1; }

    public int getCorrectGuessesP2() { return correctGuessesP2; }
    public void setCorrectGuessesP2(int correctGuessesP2) { this.correctGuessesP2 = correctGuessesP2; }

    public List<String> getGuessesP1() { return guessesP1; }
    public void setGuessesP1(List<String> guessesP1) { this.guessesP1 = guessesP1; }

    public List<String> getGuessesP2() { return guessesP2; }
    public void setGuessesP2(List<String> guessesP2) { this.guessesP2 = guessesP2; }
}





