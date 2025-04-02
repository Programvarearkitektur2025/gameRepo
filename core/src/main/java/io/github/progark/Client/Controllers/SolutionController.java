package io.github.progark.Client.Controllers;

import java.util.HashMap;
import java.util.Map;

import io.github.progark.Client.Views.Game.SolutionView;
import io.github.progark.Main;

public class SolutionController extends Controller {

    private final Main main;
    private final SolutionView solutionView;

    public SolutionController(Main main) {
        this.main = main;
        this.solutionView = new SolutionView(this);
    }

    @Override
    public void enter() {
        solutionView.enter();
    }

    @Override
    public void update(float delta) {
        solutionView.update(delta);
        solutionView.render();
    }

    @Override
    public void dispose() {
        solutionView.dispose();
    }

    // Tanken her er at i stedet for mock data brukes hash mapet som er lagret i firebase
    // Dette hash mapet skal sendes fra lobby controller til solution controller så vi slipper å utføre API kall her
    public Map<String, Integer> getCorrectAnswers() {
        Map<String, Integer> answers = new HashMap<>();
        answers.put("Andorra", 4);
        answers.put("Albania", 5);
        answers.put("Armenia", 3);
        answers.put("Austria", 6);
        answers.put("Australia", 7);
        // Add more answers and points as needed
        return answers;
    }

    // Usikker på om de skal tilbake til home når de trykker tilbake?
    public void goBackToHome() {
        main.useHomeController();
    }
}
