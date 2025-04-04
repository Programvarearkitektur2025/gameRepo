package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Menu.CreateGameView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.CreateGameService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class CreateGameController extends Controller {

    private final AuthService authService;
    private final Main main;
    private CreateGameView createGameView;
    private DatabaseManager databaseManager;
    private final CreateGameService createGameService;
    // private LobbyService lobbyService;
    private GameModel lobbyModel;

    public CreateGameController(AuthService authService, DatabaseManager databaseManager, Main main) {
        this.databaseManager = databaseManager;
        this.main = main;
        this.authService = authService;
        this.lobbyModel = new GameModel();
        this.createGameView = new CreateGameView(this);
        this.createGameService = new CreateGameService(databaseManager);
    }

    @Override
    public void enter() {
        createGameView.enter();
    }

    public void update(float delta) {
        createGameView.update(delta);
        createGameView.render();
    }

    @Override
    public void dispose() {
        createGameView.dispose();
    }
    public void goBackToHome() {
        main.useHomeController();
    }

    public void viewGamePage(GameModel gameModel){
        main.useGameController(gameModel);
    }

    public void createLobby(int difficulty, int rounds, boolean multiplayer, DataCallback callBack) {
        authService.getCurrentUser(new DataCallback() {
            @Override
            public void onSuccess(Object userObj) {
                UserModel user = (UserModel) userObj;

                createGameService.createLobby(user.getUsername(), difficulty, rounds, multiplayer, new DataCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        GameModel createdLobby = (GameModel) result;

                        if (!multiplayer) {
                            lobbyModel.setPlayerTwo("");
                        }

                        // Update internal model
                        lobbyModel.setLobbyCode(createdLobby.getLobbyCode());
                        lobbyModel.setPlayerOne(createdLobby.getPlayerOne());
                        lobbyModel.setPlayerTwo(createdLobby.getPlayerTwo());
                        lobbyModel.setDifficulty(createdLobby.getDifficulty());
                        lobbyModel.setStatus(createdLobby.getStatus());
                        lobbyModel.setCreatedAt(createdLobby.getCreatedAt());
                        lobbyModel.setRounds(createdLobby.getRounds());
                        lobbyModel.setMultiplayer(createdLobby.isMultiplayer());

                        // Call the callback to notify the operation is complete
                        if (callBack != null) {
                            callBack.onSuccess(createdLobby);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Failed to create lobby: " + e.getMessage());
                        if (callBack != null) {
                            callBack.onFailure(e);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to get current user: " + e.getMessage());
                if (callBack != null) {
                    callBack.onFailure(e);
                }
            }
        });
    }


    public GameModel getLobby() {
        return lobbyModel;
    }
}
