package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Menu.UserView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.Service.AuthService;
/*
 * UserController.java
 * This class is responsible for managing user interactions and authentication.
 * It handles user sign-in, sign-out, and navigation to the login screen.
 * The controller interacts with the AuthService to perform operations related to user authentication.
 */
public class UserController extends Controller {

    private final AuthService authService;
    private final Main main;
    private UserView view;


    public UserController(AuthService authService, Main main) {
        this.authService = authService;
        this.main = main;

        this.view = new UserView(this);
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

    @Override
    public void dispose() {
        view.dispose();
    }

    public void logout() {
        authService.signOut();
        main.useLoginController();
    }

    public Main getMain() {
        return main;
    }
}
