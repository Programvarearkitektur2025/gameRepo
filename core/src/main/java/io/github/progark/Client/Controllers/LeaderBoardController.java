package io.github.progark.Client.Controllers;

import java.util.Map;
import java.util.HashMap;

import io.github.progark.Client.Views.Game.LeaderBoardView;
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

    // Tanken her er at i stedet for mock data brukes hash mapet som er lagret i firebase
    // Trenger en query som kun henter de 10 beste brukerne i synkende rekkefølge
    // Om det ikke går an å sortere så kan sorteringen gjøres her i en stream
    // Men viktig at kun de beste brukerne hentes så vi kan bevare performance i og med at ikke alle brukere skal vises uansett
    public Map<String, Integer> getLeaderboard() {
        Map<String, Integer> leaderboard = new HashMap<>();
        leaderboard.put("Emil", 45);
        leaderboard.put("Alice", 76);
        leaderboard.put("Bob", 50);
        leaderboard.put("Charlie", 88);
        leaderboard.put("Derek", 92);
        leaderboard.put("Eve", 67);
        leaderboard.put("Frank", 82);
        leaderboard.put("Grace", 90);
        leaderboard.put("Hannah", 80);
        leaderboard.put("Ivy", 55);
        return leaderboard;
    }

    // Henter både poengene og brukernavnet til den innloggede brukeren fra firebase
    // Her trengs en query som henter den innloggede brukeren og dens score
    // Dette kan selvfølgelig slås sammen til en funksjon om ønskelig
    public String getLoggedInUser() {
        return "Marcus";  // Mocked user
    }
    public int getLoggedInUserPoints() {
        return 100;
    }

    // Tilbakeknapp fører bruker hjem
    public void goBackHome() {
        main.useHomeController();
    }
}
