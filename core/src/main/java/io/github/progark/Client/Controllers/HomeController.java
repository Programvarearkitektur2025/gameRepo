package io.github.progark.Client.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.HomeModel;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.GameService;
import io.github.progark.Server.Service.HomeService;
import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;
/*
 *  HomeController.java
 * This class is responsible for managing the home screen of the application.
 * It handles user authentication, game navigation, and interaction with the home service.
 * It also manages the loading and storing of game models.
 */
public class HomeController extends Controller {
    private HomeModel model;
    private HomeView view;
    private final AuthService authService;
    private final Main main;
    private final HomeService homeService;
    private final GameService gameService;
    private final DatabaseManager databaseManager;
    private final Map<String, GameModel> loadedGames = new HashMap<>();


    public HomeController(AuthService authService, Main main, DatabaseManager databaseManager) {
        this.authService = authService;
        this.main = main;
        this.databaseManager = databaseManager;
        this.model = new HomeModel();
        this.view = new HomeView(this);
        this.homeService = new HomeService(databaseManager);
        this.gameService = new GameService(databaseManager);
    }

    @Override
    public void enter() {
        view.enter();
    }

    @Override
    public void update(float delta) {
        view.update(delta);
        view.render();
    }

    public void storeLoadedGame(String gameId, GameModel game) {
        loadedGames.put(gameId, game);
    }

    public boolean isMultiplayer(String gameId) {
        GameModel game = loadedGames.get(gameId);
        return game != null && game.isMultiplayer();
    }


    @Override
    public void dispose() {
        view.dispose();
    }

    public void ViewJoinGamePage() {
        main.useJoinGameController();
    }

    public void ViewCreateGamePage() {
        main.useCreateGameController();
    }

    public void ViewGamePage(GameModel gameLobby) {

        main.useGameController(gameLobby);
    }

    public Main getMain() {
        return main;
    }

    public void ViewUserPage() {
        main.useUserController();
    }

    public void openTutorialPage() {
        main.useTutorialController();
    }

    public void navigateToHome() {
        main.useHomeController();
    }

    public void navigateToLeaderboard() {
        main.useLeaderBoardController();
    }

    public void navigateToSettings() {
        main.useSettingsController();
    }

    public void getLoggedInUserHome(DataCallback callback) {
        authService.getLoggedInUsername(callback);
    }
/*
 * getRelevantGames
 * This method retrieves the relevant games for the logged-in user.
 * It uses the AuthService to get the current user and then fetches the relevant games from the GameService.
 */
    public void getRelevantGames(DataCallback callback) {
        authService.getCurrentUser(new DataCallback() {
            @Override
            public void onSuccess(Object userObj) {
                UserModel user = (UserModel) userObj;

                gameService.getRelevantGames(user, new DataCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        if (data instanceof List) {
                            callback.onSuccess(data);
                        } else {
                            callback.onFailure(new Exception("Unexpected data format from GameService"));
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        callback.onFailure(e);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(new Exception("Failed to fetch logged-in user: " + e.getMessage()));
            }
        });
    }

    /*
     * getGameToOpen
     * This method retrieves a game to open based on the provided lobby code.
     * It uses the HomeService to fetch the game data and then updates the main controller with the game.
     */
    public void getGameToOpen(String lobbyCode) {
        homeService.getGameByLobbyCode(lobbyCode, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                if (result instanceof Map) {
                    GameModel game = GameModel.fromMap(lobbyCode, (Map<String, Object>) result);
                    main.useGameController(game);
                } else {
                    System.out.println("Invalid game data format from getGameToOpen.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to fetch game: " + e.getMessage());
            }
        });
    }


}
