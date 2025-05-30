package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Menu.SettingsView;
import io.github.progark.Main;
/*
 * SettingsController.java
 * This class is responsible for managing the settings screen of the application.
 * It handles user interactions with the settings view and provides navigation back to the home screen.
 */
public class SettingsController extends Controller {

    private final Main main;
    private SettingsView view;

    public SettingsController(Main main) {
        this.main = main;
        this.view = new SettingsView(this);
    }

    @Override
    public void enter() {
        view.enter();
    }
    public Main getMain() {
        return main;
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

    public void goBackHome() {
        main.useHomeController();
    }

}
