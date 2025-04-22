package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Menu.TutorialView;
import io.github.progark.Main;
/*
 * TutorialController.java
 * This class is responsible for managing the tutorial screen of the application.
 * It handles user interactions with the tutorial view and provides navigation back to the home screen.
 * It also manages the loading and storing of game models.
 * The controller interacts with the TutorialView to perform operations related to the tutorial.
 */
public class TutorialController extends Controller {

    private final Main main;
    private TutorialView tutorialView;

    public TutorialController(Main main) {
        this.main = main;
        this.tutorialView = new TutorialView(this);
    }

    @Override
    public void enter() {
        tutorialView.enter();
    }

    @Override
    public void update(float delta) {
        tutorialView.update(delta);
        tutorialView.render();
    }

    @Override
    public void dispose() {
        tutorialView.dispose();
    }

    public void goBackToHome() {
        main.useHomeController();
    }
}
