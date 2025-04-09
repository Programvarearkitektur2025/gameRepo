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





}
