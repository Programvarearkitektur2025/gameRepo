package io.github.progark.Client.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.GameService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class GameController extends Controller {

    private GameModel gameModel;
    private GameView gameView;
    private Main main;
    private GameService gameService;

    public GameController(DatabaseManager databaseManager, Main main, GameModel gameModel) {
        this.gameModel = gameModel;
        this.main = main;
        this.gameService = new GameService(databaseManager);
        for (RoundModel round : getGames()){
            System.out.println("Round from GameController: " +round.getQuestion() );
        }
    }

    @Override
    public void enter() {
        if (gameView == null) {
            gameView = new GameView(this);
        }
        gameView.enter();
    }

    @Override
    public void update(float delta) {
        if (gameView != null) {
            gameView.update(delta);
            gameView.render();
        }
    }

    @Override
    public void dispose() {
        if (gameView != null) {
            gameView.dispose();
            gameView = null;
        }
    }

    public void goToHome() {
        main.useHomeController();
    }

    public void goToRound() {
        final boolean loadViewImmediately = true;

        if (loadViewImmediately) {
            // Transition first, then update once round is fetched
            main.useRoundController(gameModel);
        }

        gameService.fetchCurrentRound(gameModel, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof RoundModel)) {
                    System.err.println("Fetched data is not a RoundModel: " + (data != null ? data.getClass().getSimpleName() : "null"));
                    return;
                }

                RoundModel round = (RoundModel) data;
                int index = (int) gameModel.getCurrentRound();

                // Defensive: Ensure games list is not null
                List<RoundModel> rounds = gameModel.getGames();
                if (rounds == null) {
                    System.out.println("Games list was null, initializing a new one.");
                    rounds = new ArrayList<>();
                }

                // Add or replace round
                if (index >= 0 && index < rounds.size()) {
                    System.out.println("Updating round at index " + index);
                    rounds.set(index, round);
                } else {
                    System.out.println("Adding round at index " + index + " (list size: " + rounds.size() + ")");
                    // Fill gaps if needed (avoid IndexOutOfBounds)
                    while (rounds.size() < index) {
                        rounds.add(null);
                    }
                    rounds.add(round);
                }

                gameModel.setGames(rounds);

                if (!loadViewImmediately) {
                    main.useRoundController(gameModel); // Only if we waited to transition
                } else {
                    System.out.println("Round data updated post-view load");
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Failed to fetch current round: " + e.getMessage());
            }
        });
    }


    public void goToRoundSingleplayer(){
        main.useRoundController(gameModel);
    }

    // Gettere
    public String getLobbyCode() {
        return gameModel.getLobbyCode();
    }

    public String getPlayerOne() {
        return gameModel.getPlayerOne();
    }

    public String getPlayerTwo() {
        return gameModel.getPlayerTwo();
    }

    public int getDifficulty() {
        return gameModel.getDifficulty();
    }

    public int getRounds() {
        return gameModel.getRounds();
    }

    public Number getPlayerOnePoints() {
        return gameModel.getPlayerOnePoints();
    }

    public Number getPlayerTwoPoints() {
        return gameModel.getPlayerTwoPoints();
    }

    public List<RoundModel> getGames() {
        return gameModel.getGames();
    }

    public boolean isMultiplayer() {
        return gameModel.isMultiplayer();
    }

    public void checkForRounds() {
        if (gameModel.getGames().isEmpty()) {
            initGameRounds();
        }
    }

    public void initGameRounds() {

        gameService.loadRoundsFromFirebase(gameModel, gameModel.getRounds(), new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                System.out.println("Game rounds loaded successfully.");
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Failed to load game rounds: " + e.getMessage());
            }
        });
    }

    public void setActiveRoundIndex(int index) {
        gameModel.setActiveRound(index);
    }


    public void updateLeaderBoard(){
        gameService.updateLeaderBoard(gameModel, main.getAuthService());
    }

    public void goToLeaderBoard(){
        main.useLeaderBoardController();
    }

    public void whoAmI(DataCallback callback) {
        AuthService authService = main.getAuthService();
        authService.getCurrentUser(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                UserModel user = (UserModel) data;
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Error fetching user: " + e);
                callback.onFailure(e);
            }
        });

    }
    public int getCurrentRoundIndex(){
        List<RoundModel> games = getGames();
        int roundIndex=0;
        for (RoundModel round : games){
            if (round.hasBothPlayersAnswered()) roundIndex++;
        }
        return roundIndex;
    }
}
