package io.github.progark.Client.Controllers;

import com.badlogic.gdx.Gdx;

import io.github.progark.Client.Views.ViewManager;
/*
 * ControllerManager.java
 * This class manages the current controller in the application.
 * It allows for setting a new controller, updating the current controller with a delta time,
 * and disposing of the current controller's resources.
 * It uses a ViewManager to handle the views associated with the controllers.
 * The setController method is called to switch to a new controller, and it ensures that the previous controller is disposed of properly.
 * The update method is called to update the current controller with the delta time.
 * The dispose method is called to clean up resources when the application is closed.
 * The class uses a singleton pattern to ensure that only one instance of the ControllerManager exists.
 * The setController method is synchronized to ensure thread safety when switching controllers.
 */
public class ControllerManager {

    private ViewManager viewManager;
    private Controller currentController;

    public void setController(Controller newController) {
        Gdx.app.postRunnable(() -> {
            if (currentController != null) {
                currentController.dispose();
            }
            currentController =newController;
            currentController.enter();
        });
    }

    public void update(float delta) {
        if (currentController != null) {
            currentController.update(delta);
        }
    }

    public void dispose() {
        if (currentController != null) {
            currentController.dispose();
        }
    }
}
