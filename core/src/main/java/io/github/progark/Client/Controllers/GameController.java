package io.github.progark.Client.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.RoundModel;
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
        gameService.fetchCurrentRound(gameModel, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof RoundModel) {
                    int index = (int) gameModel.getCurrentRound();
                    List<RoundModel> rounds = gameModel.getGames();

                    if (index >= 0 && index < rounds.size()) {
                        rounds.set(index, (RoundModel) data); // Update existing round
                    } else {
                        // If out of bounds, just add it (edge case)
                        rounds.add((RoundModel) data);
                    }

                    gameModel.setGames(rounds); // Apply updated list
                }

                main.useRoundController(gameModel); // Now go to the round
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("❌ Failed to fetch current round: " + e.getMessage());
                // Optionally still proceed, or show error to user
            }
        });
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

    public GameModel getGameModel() {
        return this.gameModel;
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

    public int getCurrentRound(){
        return (int) gameModel.getCurrentRound();
    }






}
