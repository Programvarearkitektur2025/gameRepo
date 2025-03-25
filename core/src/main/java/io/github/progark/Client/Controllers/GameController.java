package io.github.progark.Client.Controllers;


import io.github.progark.Client.Model.GameModel;
import io.github.progark.Client.Service.GameService;
import io.github.progark.Client.Views.Game.GameView;

public class GameController {
    private GameService gameService;
    private GameModel gameModel;
    private GameView gameView;

    public GameController(GameService gameService, GameModel gameModel, GameView gameView) {
        this.gameService = gameService;
        this.gameModel = gameModel;
        this.gameView = gameView;
    }
}
