// JoinGameController.java
package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Menu.JoinGameView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.CreateGameService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

import java.util.Map;
/*
 * JoinGameController.java
 * This class is responsible for managing the joining of game lobbies.
 * It handles user authentication, lobby joining, and navigation to the game page.
 * It also manages the loading and storing of game models.
 * The controller interacts with the CreateGameService to perform operations related to game lobbies.
 */
public class JoinGameController extends Controller {

    private final Main main;
    private final AuthService authService;
    private final JoinGameView joinGameView;
    private final CreateGameService createGameService;
    private final DatabaseManager databaseManager;

    public JoinGameController(AuthService authService, DatabaseManager databaseManager, Main main) {
        this.authService = authService;
        this.databaseManager = databaseManager;
        this.joinGameView = new JoinGameView(this);
        this.createGameService = new CreateGameService(databaseManager);
        this.main = main;
    }

    public void joinLobby(String lobbyCode) {
        authService.getCurrentUser(new DataCallback() {
            @Override
            public void onSuccess(Object userObj) {
                UserModel user = (UserModel) userObj;

                createGameService.joinLobby(lobbyCode, user.getUsername(), new DataCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        GameModel lobby = (GameModel) result;


                        viewGamePage(lobby);
                    }


                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Failed to join lobby: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to fetch user: " + e.getMessage());
            }
        });
    }

    public void viewGamePage(GameModel gameModel){
        main.useGameController(gameModel);
    }

    @Override
    public void enter() {
        joinGameView.enter();
    }

    @Override
    public void update(float delta) {
        joinGameView.update(delta);
        joinGameView.render();
    }
    public void goBackToHome() {
        main.useHomeController();
    }

    @Override
    public void dispose() {
        joinGameView.dispose();
    }
}
