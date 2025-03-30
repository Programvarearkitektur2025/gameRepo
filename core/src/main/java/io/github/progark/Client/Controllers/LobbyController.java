package io.github.progark.Client.Controllers;

import java.sql.Timestamp;
import java.util.List;

import io.github.progark.Client.Views.Game.LobbyView;
import io.github.progark.Server.Model.Game.LobbyModel;
import io.github.progark.Server.Model.Game.ResultModel;
import io.github.progark.Server.Service.LobbyService;
import io.github.progark.Client.Views.Game.ResultView;
import io.github.progark.Server.database.DataCallback;

public class LobbyController {

    private LobbyModel lobbyModel;
    private LobbyService lobbyService;
    private LobbyView lobbyView;

    public LobbyController(LobbyModel lobbyModel,
                           LobbyService lobbyService,
                           LobbyView lobbyView) {
        this.lobbyModel = lobbyModel;
        this.lobbyService = lobbyService;
        this.lobbyView = lobbyView;
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


        // Oppdaterer view (dersom du trenger umiddelbar oppdatering)
        // lobbyView.updateResultModel(resultModel);
    }

    public void saveResult() {
    }

    public void loadResult(String gameId) {
        lobbyService.loadResult(gameId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                /*
                if (data instanceof ResultModel) {
                    ResultModel loaded = (ResultModel) data;
                    resultModel = loaded;
                    // Oppdaterer view for resultat etter runden
                }

                 */
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
