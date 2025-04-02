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
    private HomeService homeService;

    public HomeController(AuthService authService, Main main, DatabaseManager databaseManager) {
        this.authService = authService;
        this.main = main;
        this.model = new HomeModel();
        this.view = new HomeView(this);
        this.homeService = new HomeService(databaseManager);
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


}
