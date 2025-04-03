package io.github.progark.Client.Controllers;

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

public class HomeController extends Controller {
    private HomeModel model;
    private HomeView view;
    private final AuthService authService;
    private final Main main;
    private HomeService homeService;
    private GameService gameService;

    public HomeController(AuthService authService, Main main, DatabaseManager databaseManager) {
        this.authService = authService;
        this.main = main;
        this.model = new HomeModel();
        this.view = new HomeView(this);
        this.homeService = new HomeService(databaseManager);
        this.gameService = new GameService(databaseManager);
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

    public void ViewJoinGamePage(){
        main.useJoinGameController();
    }
    public void ViewCreateGamePage(){
        main.useCreateGameController();
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

    public void getRelevantGames(DataCallback callback){
        // This is not completely architectural correct as this is a service task, not a controller task.
        authService.getCurrentUser(new DataCallback() {
            @Override
            public void onSuccess(Object userObj) {
                UserModel user = (UserModel) userObj;


                gameService.getRelevantGames(user, new DataCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        List<Map<String, GameModel>> createdLobby = (List<Map<String, GameModel>>) result;
                        callback.onSuccess(createdLobby);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        callback.onFailure(e);
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
