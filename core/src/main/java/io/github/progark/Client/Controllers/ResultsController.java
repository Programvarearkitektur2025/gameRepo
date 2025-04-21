package io.github.progark.Client.Controllers;

import io.github.progark.Client.Views.Game.ResultsView;
import io.github.progark.Main;

public class ResultsController extends Controller {
    private final Main main;
    private final ResultsView resultsView;

    public ResultsController(Main main) {
        this.main = main;
        this.resultsView = new ResultsView(main);
        enter();
    }

    @Override
    public void enter() {
        resultsView.enter();
    }

    @Override
    public void update(float delta) {
        resultsView.update(delta);
        resultsView.render();
    }

    @Override
    public void dispose() {
        resultsView.dispose();
    }
}
