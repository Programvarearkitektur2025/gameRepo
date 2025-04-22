package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Login.RegistrationView;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;
/*
 * RegistrationController.java
 * This class is responsible for managing the user registration process.
 * It handles user authentication, navigation to the login page,
 * and interaction with the AuthService.
 */
public class RegistrationController extends Controller {
    private final AuthService authService;
    private final Main main;
    private RegistrationView view;

    public RegistrationController(AuthService authService, Main main) {
        this.authService = authService;
        this.main = main;
        this.view = new RegistrationView(this);  // The view is created here and linked to the controller.
    }

    @Override
    public void enter() {
            view.enter();
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
 * registerUser
 * This method is responsible for registering a new user.
 * It retrieves the user's email, password, and username from the view,
 * and calls the AuthService to perform the sign-up operation.
 * If the sign-up is successful, it navigates to the login page.
 */
    public void registerUser(String email,String password, String username){
        authService.signUp(email, password, username, new DataCallback() {
            @Override
            public void onSuccess(Object message) {
                System.out.println("Registration successful!");
                main.useLoginController();
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Registration failed: " + e.getMessage());
            }
        });
    }

    public void viewLoginPage(){
        main.useLoginController();
    }

}

