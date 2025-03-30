package io.github.progark;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import io.github.progark.Client.Controllers.ControllerManager;
import io.github.progark.Client.Controllers.HomeController;
import io.github.progark.Client.Controllers.LoginController;
import io.github.progark.Client.Controllers.RegistrationController;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DatabaseManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private AuthService authService;
    private ControllerManager controllerManager;
    private DatabaseManager databaseManager;

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

    public void useLoginController(){
        controllerManager.setController(new LoginController(authService,this));
    }
    public void useHomeController(){
        controllerManager.setController(new HomeController(authService, this));
    }
    public void useRegisterController(){
        controllerManager.setController(new RegistrationController(authService, this));
    }
    public void useJoinGameController(){
        //controllerManager.setController(new JoinGameController((authService,this));
    }
    public void useCreateGameController(){
        //controllerManager.setController(new CreateGameController((authService,this));
    }
}
