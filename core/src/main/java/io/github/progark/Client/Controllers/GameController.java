package io.github.progark.Client.Controllers;

import java.util.List;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Service.GameService;
import io.github.progark.Server.database.DatabaseManager;

public class GameController extends Controller {

    private GameModel lobbyModel;
    private GameService lobbyService;
    private GameView lobbyView;

    public GameController(DatabaseManager databaseManager, Main main) {
        this.lobbyView = new GameView(this);
        this.lobbyService = new GameService(databaseManager);
    }

    public void setLobbyView(GameView lobbyView) {
        this.lobbyView = lobbyView;
    }

    public String getLobbyCode() {
        return lobbyModel.getLobbyCode();
    }

    public String getPlayerOne() {
        return lobbyModel.getPlayerOne();
    }

    public String getPlayerTwo() {
        return lobbyModel.getPlayerTwo();
    }

    public int getDifficulty() {
        return lobbyModel.getDifficulty();
    }

    public int getRounds() {
        return lobbyModel.getRounds();
    }

    public Number getPlayerOnePoints() {
        return lobbyModel.getPlayerOnePoints();
    }

    public Number getPlayerTwoPoints() {
        return lobbyModel.getPlayerTwoPoints();
    }

    public List<RoundModel> getGames() {
        return lobbyModel.getGames();
    }

    @Override
    public void enter() {
        lobbyView.enter();
    }

    @Override
    public void update(float delta) {
        lobbyView.update(delta);
        lobbyView.render();
    }

    @Override
    public void dispose() {
        lobbyView.dispose();
    }
}
