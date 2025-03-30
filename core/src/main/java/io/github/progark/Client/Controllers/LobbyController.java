package io.github.progark.Client.Controllers;

import java.sql.Timestamp;
import java.util.List;

import io.github.progark.Client.Views.Game.GameView;
import io.github.progark.Client.Views.Game.LobbyView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.LobbyModel;
import io.github.progark.Server.Service.LobbyService;

public class LobbyController extends Controller {

    private LobbyModel lobbyModel;
    private LobbyService lobbyService;
    private LobbyView lobbyView;

    public LobbyController(LobbyModel lobbyModel, Main main) {
        this.lobbyModel = lobbyModel;
        this.lobbyView = new LobbyView(this);
    }

    public void setLobbyView(LobbyView lobbyView) {
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

    public List<GameModel> getGames() {
        return lobbyModel.getGames();
    }

    @Override
    public void enter() {
        lobbyView.enter();
    }

    @Override
    public void update(float delta) {
        lobbyView.update(delta);
        //view.handleInput();
        lobbyView.render();
    }

    @Override
    public void dispose() {
        lobbyView.dispose();
    }
}
