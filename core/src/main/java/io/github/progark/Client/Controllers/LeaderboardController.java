package io.github.progark.Client.Controllers;

import java.util.Map;

import io.github.progark.Client.Views.Game.LeaderBoardView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.LeaderboardModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.LeaderboardService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class LeaderboardController extends Controller {

    private final LeaderBoardView view;
    private final LeaderboardService leaderboardService;
    private final Main main;
    private LeaderboardModel leaderboardModel;

    public LeaderboardController(DatabaseManager databaseManager, AuthService authService, Main main) {
        this.main = main;
        this.leaderboardService = new LeaderboardService(databaseManager, authService);
        this.view = new LeaderBoardView(this);
    }

    @Override
    public void enter() {
        view.enter();
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

    public void loadLeaderboard(DataCallback dataCallback) {
        leaderboardService.loadLeaderboard(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof LeaderboardModel) {
                    leaderboardModel = (LeaderboardModel) data;
                    System.out.println(leaderboardModel);
                    for (Map.Entry<String, Integer> entry : leaderboardModel.getUserScore().entrySet()) {
                        System.out.println(entry);
                    }

                    Map<String, Integer> unsorted = leaderboardModel.getUserScore();

                    Map<String, Integer> sorted = unsorted.entrySet()
                            .stream()
                            .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // descending
                            .collect(java.util.stream.Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (e1, e2) -> e1,
                                    java.util.LinkedHashMap::new
                            ));

                    leaderboardModel.setUserScore(sorted);


                    dataCallback.onSuccess(leaderboardModel);
                    System.out.println("Loaded " + leaderboardModel.getUserScore().size() + " users into leaderboard.");

                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Failed to load leaderboard:");
                e.printStackTrace();
            }
        });
    }

    public void incrementScoreFor(String userId) {
        leaderboardService.incrementUserScore(userId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                //loadLeaderboard();
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main getMain() {
        return main;
    }


}
