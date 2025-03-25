package io.github.progark.Client.Controllers;

import java.sql.Timestamp;
import java.util.List;

import io.github.progark.Client.Model.ResultModel;
import io.github.progark.Client.Service.ResultService;
import io.github.progark.Client.Views.Game.ResultView;
import io.github.progark.Server.database.DataCallback;


public class ResultController {
    private ResultService resultService;
    private ResultModel resultModel;
    private ResultView resultView;

    public ResultController (ResultService resultService, ResultModel resultModel, ResultView resultView){
        this.resultModel = resultModel;
        this.resultService = resultService;
        this.resultView = resultView;
    }

    public void updateResult(String winner,
                            String loser,
                            int winnerScore,
                            int loserScore,
                            List<String> questions,
                            int numberOfGuessesP1,
                            int numberOfGuessesP2,
                            int correctGuessesP1,
                            int correctGuessesP2,
                            List<String> guessesP1,
                            List<String> guessesP2) {

        resultModel.setWinner(winner);
        resultModel.setLoser(loser);
        resultModel.setWinnerScore(winnerScore);
        resultModel.setLoserScore(loserScore);
        resultModel.setFinishedAt(new Timestamp(System.currentTimeMillis()));
        resultModel.setQuestions(questions);
        resultModel.setNumberOfGuessesP1(numberOfGuessesP1);
        resultModel.setNumberOfGuessesP2(numberOfGuessesP2);
        resultModel.setCorrectGuessesP1(correctGuessesP1);
        resultModel.setCorrectGuessesP2(correctGuessesP2);
        resultModel.setGuessesP1(guessesP1);
        resultModel.setGuessesP2(guessesP2);
    }
    public void saveResult() {
        resultService.saveResult(
            resultModel.getGameId(),
            resultModel.getWinner(),
            resultModel.getLoser(),
            resultModel.getWinnerScore(),
            resultModel.getLoserScore(),
            resultModel.getQuestions(),
            resultModel.getNumberOfGuessesP1(),
            resultModel.getNumberOfGuessesP2(),
            resultModel.getCorrectGuessesP1(),
            resultModel.getCorrectGuessesP2(),
            resultModel.getGuessesP1(),
            resultModel.getGuessesP2(),
            new DataCallback() {
                @Override
                public void onSuccess(Object data) {
                    System.out.println("Result saved: " + data);
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            }
        );
    }
    public void loadResult(String gameId) {
        resultService.loadResult(gameId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof java.util.Map) {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> mapData = (java.util.Map<String, Object>) data;
                    ResultModel loaded = ResultModel.fromMap(gameId, mapData);

                    // Oppdaterer resultModel
                    resultModel.setWinner(loaded.getWinner());
                    resultModel.setLoser(loaded.getLoser());
                    resultModel.setWinnerScore(loaded.getWinnerScore());
                    resultModel.setLoserScore(loaded.getLoserScore());
                    resultModel.setFinishedAt(loaded.getFinishedAt());
                    resultModel.setQuestions(loaded.getQuestions());
                    resultModel.setNumberOfGuessesP1(loaded.getNumberOfGuessesP1());
                    resultModel.setNumberOfGuessesP2(loaded.getNumberOfGuessesP2());
                    resultModel.setCorrectGuessesP1(loaded.getCorrectGuessesP1());
                    resultModel.setCorrectGuessesP2(loaded.getCorrectGuessesP2());
                    resultModel.setGuessesP1(loaded.getGuessesP1());
                    resultModel.setGuessesP2(loaded.getGuessesP2());


                    resultView.update(resultModel);
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
