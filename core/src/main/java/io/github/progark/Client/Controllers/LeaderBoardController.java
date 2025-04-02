package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Menu.LeaderBoardView;
import io.github.progark.Main;

public class LeaderBoardController extends Controller {

    private final Main main;
    private LeaderBoardView view;

    public LeaderBoardController(Main main) {
        this.main = main;
        this.view = new LeaderBoardView(this);
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
