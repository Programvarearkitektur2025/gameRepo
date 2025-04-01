package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Menu.TutorialView;
import io.github.progark.Main;

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
