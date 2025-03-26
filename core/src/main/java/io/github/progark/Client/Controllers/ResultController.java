package io.github.progark.Client.Controllers;

import java.sql.Timestamp;
import java.util.List;

import io.github.progark.Client.Model.ResultModel;
import io.github.progark.Client.Service.ResultService;
import io.github.progark.Client.Views.Game.ResultView;
import io.github.progark.Server.database.DataCallback;

public class ResultController {
    private ResultModel resultModel;
    private ResultService resultService;
    private ResultView resultView;

    public ResultController(ResultModel resultModel,
                            ResultService resultService,
                            ResultView resultView) {
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

        // Oppdaterer view (dersom du trenger umiddelbar oppdatering)
        resultView.update(resultModel);
    }

    public void saveResult() {
        resultService.saveResult(resultModel, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                System.out.println("Result saved: " + data);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadResult(String gameId) {
        resultService.loadResult(gameId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof ResultModel) {
                    ResultModel loaded = (ResultModel) data;
                    resultModel = loaded;
                    // Oppdaterer view
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
