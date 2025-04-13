package io.github.progark;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import io.github.progark.Client.Controllers.ControllerManager;
import io.github.progark.Client.Controllers.CreateGameController;
import io.github.progark.Client.Controllers.GameController;
import io.github.progark.Client.Controllers.ResultsController;
import io.github.progark.Client.Controllers.RoundController;
import io.github.progark.Client.Controllers.HomeController;
import io.github.progark.Client.Controllers.JoinGameController;
import io.github.progark.Client.Controllers.LeaderboardController;
import io.github.progark.Client.Controllers.LoginController;
import io.github.progark.Client.Controllers.RegistrationController;
import io.github.progark.Client.Controllers.SettingsController;
import io.github.progark.Client.Controllers.SolutionController;
import io.github.progark.Client.Controllers.TutorialController;
import io.github.progark.Client.Controllers.UserController;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DatabaseManager;
import io.github.progark.Client.Audio.MusicManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private AuthService authService;
    private ControllerManager controllerManager;
    private DatabaseManager databaseManager;
    private final MusicManager musicManager = new MusicManager();


    public Main(DatabaseManager dbManager, AuthService authManager) {
        if (dbManager == null || authManager == null) {
            throw new IllegalArgumentException("DatabaseManager and AuthService cannot be null");
        }
        this.databaseManager = dbManager;
        this.authService = authManager;
    }

    @Override
    public void create() {
        controllerManager = new ControllerManager();
        useLoginController();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        controllerManager.update(delta);
    }

    @Override
    public void dispose() {
        if (controllerManager != null) {
            controllerManager.dispose();
        }
        super.dispose();
    }

    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void useLoginController() {
        controllerManager.setController(new LoginController(authService, this));
    }

    public void useHomeController() {
        controllerManager.setController(new HomeController(authService, this, databaseManager));
    }

    public void useRegisterController() {
        controllerManager.setController(new RegistrationController(authService, this));
    }

    public void useJoinGameController() {
        controllerManager.setController(new JoinGameController(authService, databaseManager, this));
    }

    public void useCreateGameController() {
        controllerManager.setController(new CreateGameController(authService, databaseManager, this));
    }

    public void useGameController(GameModel gameModel) {
        controllerManager.setController(new GameController(databaseManager, this, gameModel));
    }

    public void useTutorialController() {
        controllerManager.setController(new TutorialController(this));
    }


    public void useUserController() {
        controllerManager.setController(new UserController(authService, this));
    }


    public void useLeaderBoardController() {
        controllerManager.setController(new LeaderboardController(databaseManager, authService, this));
    }

    public void useSettingsController() {
        controllerManager.setController(new SettingsController(this));
    }
    public MusicManager getMusicManager() {
        return musicManager;
    }

    public void useSolutionController() {
        controllerManager.setController(new SolutionController(this));
    }

    public void useResultsController() {
        controllerManager.setController(new ResultsController(this));
    }

    public void useRoundController(GameModel gameModel) {
        controllerManager.setController(new RoundController(gameModel,databaseManager, this, authService));
    }

    public void returnToGameControllerFromRound(RoundModel roundModel, GameModel gameModel){

    }
}
