package io.github.progark.Client.Controllers;

import java.util.Map;

import io.github.progark.Main;
import io.github.progark.Server.Model.Game.HomeModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.HomeService;
import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class HomeController extends Controller {
    private HomeModel model;
    private HomeView view;
    private final AuthService authService;
    private final Main main;

    public HomeController(AuthService authService, Main main) {
        this.authService = authService;
        this.main = main;
        this.model = new HomeModel();
        this.view = new HomeView(this);
    }
    @Override
    public void enter() {
        // When this controller becomes active, we can perform actions like checking user status.
        //checkUserStatus();
        view.enter();
    }

    @Override
    public void update(float delta) {
        // Update the view (and possibly perform other periodic logic)
        view.update(delta);
        //view.handleInput();
        view.render();
    }

    @Override
    public void dispose() {
        view.dispose();
    }

    /*
    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }
     */

    public void loadUserGames(String userId) {
        /*
        homeService.loadUserGames(userId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof HomeModel) {
                    System.out.println(data);
                    homeModel = (HomeModel) data;
                    if (homeView != null) {
                        homeView.updateGameLists(homeModel);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to load games: " + e.getMessage());
            }
        });
         */

    }

    public void createNewGame(String userId, String opponentId) {
        /*
        homeService.createNewGame(userId, opponentId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String gameId = (String) data;
                System.out.println("Created new game with ID: " + gameId);
                loadUserGames(userId); // Reload games list
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to create game: " + e.getMessage());
            }
        });

         */
    }

    public void handleGameEntryClick(String gameId) {
        // Handle game entry click - navigate to game view
        /*
        if (homeView != null) {
            homeView.navigateToGame(gameId);
        }

         */
    }
    public void ViewJoinGamePage(){
        main.useJoinGameController();
    }
    public void ViewCreateGamePage(){
        main.useCreateGameController();
    }
    public Main getMain() {
        return main;
    }
    public void ViewGamePage(){
        main.useGameController();
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


}
