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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
/*
 * GameController.java
 * This class is responsible for managing the game state and interactions.
 * It handles the game rounds, player interactions, and synchronization with Firebase.
 */
public class GameController extends Controller {

    private GameModel gameModel;
    private GameView gameView;
    private Main main;
    private GameService gameService;
    private Task syncTask;
    private boolean leaderboardAlreadyUpdated = false;


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
        startGameSyncing();
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
            main.useRoundController(gameModel);
        }
        stopGameSyncing();
    }


    public void goToRoundSingleplayer(){
        stopGameSyncing();
        main.useRoundController(gameModel);
    }

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
/*
 * initGameRounds
 * This method initializes the game rounds by loading them from Firebase.
 * It uses the GameService to fetch the rounds and updates the game model accordingly.
 */
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
        stopGameSyncing();
        main.useLeaderBoardController();
    }
/*
 * whoAmI
 * This method retrieves the current user from the AuthService.
 * It uses a callback to handle the success or failure of the operation.
 */
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
/*
 * getCurrentRoundIndex
 * This method calculates the current round index based on the game model.
 * It iterates through the rounds and checks if both players have answered.
 * If all rounds are completed, it updates the leaderboard.
 */

    public int getCurrentRoundIndex() {
        List<RoundModel> games = getGames();
        int roundIndex = 0;

        for (RoundModel round : games) {
            if (round.hasBothPlayersAnswered()) {
                roundIndex++;
            }
        }

        if (roundIndex == gameModel.getRounds() && !gameModel.isLeaderboardUpdated()) {
            updateLeaderBoard();
            gameModel.setLeaderboardUpdated(true);
        }

        return roundIndex;
    }




    public void startGameSyncing() {
        if (syncTask != null) return;

        syncTask = Timer.schedule(new Task() {
            @Override
            public void run() {
                gameService.loadExistingRoundsFromFirebase(gameModel, new DataCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        if (data instanceof List<?>) {
                            @SuppressWarnings("unchecked")
                            List<RoundModel> updatedRounds = (List<RoundModel>) data;
                            gameModel.setGames(updatedRounds);
                            System.out.println("✅ Synced game rounds from Firebase.");

                            if (gameView != null) {
                                gameView.onRoundsUpdated();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("❌ Failed to sync rounds from Firebase: " + e.getMessage());
                    }
                });
            }
        }, 0, 3);
    }

    public void stopGameSyncing() {
        if (syncTask != null) {
            syncTask.cancel();
            syncTask = null;
        }
    }

    private boolean allRoundsCompleted() {
        for (RoundModel round : gameModel.getGames()) {
            if (!round.hasBothPlayersAnswered()) {
                return false;
            }
        }
        return true;
    }

}
