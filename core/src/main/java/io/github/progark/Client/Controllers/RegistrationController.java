package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Login.RegistrationView;
import io.github.progark.Main;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;

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

