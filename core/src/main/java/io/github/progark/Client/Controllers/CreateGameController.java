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


    public void createLobby(int difficulty, int rounds, boolean multiplayer) {
        authService.getCurrentUser(new DataCallback() {
            @Override
            public void onSuccess(Object userObj) {
                UserModel user = (UserModel) userObj;

                createGameService.createLobby(user.getUsername(), difficulty, rounds, multiplayer, new DataCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        GameModel createdLobby = (GameModel) result;

                        // Set internal model to match
                        lobbyModel.setLobbyCode(createdLobby.getLobbyCode());
                        lobbyModel.setPlayerOne(createdLobby.getPlayerOne());
                        lobbyModel.setPlayerTwo(createdLobby.getPlayerTwo());
                        lobbyModel.setDifficulty(createdLobby.getDifficulty());
                        lobbyModel.setStatus(createdLobby.getStatus());
                        lobbyModel.setCreatedAt(createdLobby.getCreatedAt());
                        lobbyModel.setRounds(createdLobby.getRounds());
                        lobbyModel.setMultiplayer(createdLobby.isMultiplayer());

                        System.out.println("Lobby created: " + createdLobby);

                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Failed to create lobby: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to fetch user for lobby: " + e.getMessage());
            }
        });
    }
}
