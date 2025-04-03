package io.github.progark.Client.Controllers;

import java.util.List;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Service.GameService;
import io.github.progark.Server.database.DatabaseManager;

public class GameController extends Controller {

    private GameModel gameModel;
    private GameView gameView;
    private Main main;

    public GameController(DatabaseManager databaseManager, Main main, GameModel gameModel) {
        this.gameView = new GameView(this);
        this.gameModel = gameModel;
        this.main = main;
        enter();
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

    public boolean isMultiplayer() {
        return gameModel.isMultiplayer();  // Assuming gameModel has isMultiplayer method
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

    public void goToHome() {
        main.useHomeController();
    }

    public void goToRound() {
        main.useRoundController();
    }
}
