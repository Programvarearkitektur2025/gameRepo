package io.github.progark.Server.Model.Game;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    public static ResultModel fromMap(String gameId, Map<String, Object> data) {
        String winner = (String) data.get("winner");
        String loser = (String) data.get("loser");

        int winnerScore = data.get("winnerScore") != null ? ((Number) data.get("winnerScore")).intValue() : 0;
        int loserScore  = data.get("loserScore")  != null ? ((Number) data.get("loserScore")).intValue() : 0;

        long finishedAtLong = data.get("finishedAt") != null ? ((Number) data.get("finishedAt")).longValue() : 0;
        Timestamp finishedAt = new Timestamp(finishedAtLong);

        List<String> questions = data.get("questions") != null
            ? (List<String>) data.get("questions")
            : null;
        int numberOfGuessesP1 = data.get("numberOfGuessesP1") != null ? ((Number) data.get("numberOfGuessesP1")).intValue() : 0;
        int numberOfGuessesP2 = data.get("numberOfGuessesP2") != null ? ((Number) data.get("numberOfGuessesP2")).intValue() : 0;
        int correctGuessesP1  = data.get("correctGuessesP1")  != null ? ((Number) data.get("correctGuessesP1")).intValue() : 0;
        int correctGuessesP2  = data.get("correctGuessesP2")  != null ? ((Number) data.get("correctGuessesP2")).intValue() : 0;

        List<String> guessesP1 = data.get("guessesP1") != null
            ? (List<String>) data.get("guessesP1")
            : null;
        List<String> guessesP2 = data.get("guessesP2") != null
            ? (List<String>) data.get("guessesP2")
            : null;

        return new ResultModel(gameId,
            winner,
            loser,
            winnerScore,
            loserScore,
            finishedAt,
            questions,
            numberOfGuessesP1,
            numberOfGuessesP2,
            correctGuessesP1,
            correctGuessesP2,
            guessesP1,
            guessesP2
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("winner", winner);
        map.put("loser", loser);
        map.put("winnerScore", winnerScore);
        map.put("loserScore", loserScore);
        map.put("finishedAt", finishedAt != null ? finishedAt.getTime() : 0);


        map.put("questions", questions);
        map.put("numberOfGuessesP1", numberOfGuessesP1);
        map.put("numberOfGuessesP2", numberOfGuessesP2);
        map.put("correctGuessesP1", correctGuessesP1);
        map.put("correctGuessesP2", correctGuessesP2);
        map.put("guessesP1", guessesP1);
        map.put("guessesP2", guessesP2);

        return map;
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





