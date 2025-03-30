package io.github.progark.Client.Controllers;

import com.badlogic.gdx.Gdx;

import io.github.progark.Client.Views.ViewManager;

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
