package io.github.progark.Client.Controllers;

import java.util.ArrayList;
import java.util.List;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Service.GameService;
import io.github.progark.Server.database.DatabaseManager;

public class GameController extends Controller {

    private GameModel gameModel;
    private GameService lobbyService;
    private GameView gameView;
    private GameService gameService;

    public GameController(DatabaseManager databaseManager, Main main, GameModel gameModel) {
        this.gameView = new GameView(this);
        this.lobbyService = new GameService(databaseManager);
        this.gameModel = gameModel;
    }

    public void setgameView(GameView gameView) {
        this.gameView = gameView;
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

    @Override
    public void enter() {
        gameView.enter();
    }

    @Override
    public void update(float delta) {
        gameView.update(delta);
        gameView.render();
    }

    @Override
    public void dispose() {
        gameView.dispose();
    }

    public void setGameRounds() {
        int totalRounds = gameModel.getRounds();
        List<RoundModel> rounds = new ArrayList<>();

        // Ensure the game has the correct number of rounds
        for (int i = 0; i < totalRounds; i++) {
            rounds.add(new RoundModel()); // Creating new round instances
        }

        gameModel.setGames(rounds); // Updating the GameModel with the new rounds

        if (gameService != null) {
            gameService.setNewGameRounds(gameModel, rounds); // Delegate to service for database update
        } else {
            System.out.println("GameService is not initialized.");
        }
    }
}
