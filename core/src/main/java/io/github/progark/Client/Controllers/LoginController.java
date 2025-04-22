package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Login.LoginView;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;
/*
 * LoginController.java
 * This class is responsible for managing the login process of users.
 * It handles user authentication, navigation to the home page,
 * and interaction with the AuthService.
 */
public class LoginController extends Controller {
    private final AuthService authService;
    private final Main main;
    private LoginView view;

    public LoginController(AuthService authService, Main main) {
        this.authService = authService;
        this.main = main;
        this.view = new LoginView(this);  // The view is created here and linked to the controller.
    }

    @Override
    public void enter() {
        // Check login status
        if (authService.isUserLoggedIn()) {
            // Navigate to Home once logged in.
            main.useHomeController();
        } else {
        view.enter();
        }
    }

    @Override
    public void update(float delta) {
        view.update(delta);
        //view.handleInput();
        view.render();
    }

    @Override
    public void dispose() {
        view.dispose();
    }
/*
 * logInUser
 * This method is responsible for logging in the user.
 * It retrieves the user's email and password from the view,
 * and calls the AuthService to perform the sign-in operation.
 * If the sign-in is successful, it navigates to the home page.
 */
    public void logInUser(String email, String password){
        authService.signIn(email, password, new DataCallback() {
            @Override
            public void onSuccess(Object message) {
                System.out.println("Sign-in successful! User: " + (String) message);
                main.useHomeController();
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Sign-in failed: " + e.getMessage());

            }
        });
    }

    public void ViewRegisterPage(){
        main.useRegisterController();
    }
}
