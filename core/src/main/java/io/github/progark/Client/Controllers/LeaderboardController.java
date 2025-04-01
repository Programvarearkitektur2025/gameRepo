package io.github.progark.Client.Controllers;

import io.github.progark.Server.Model.Game.LeaderboardModel;
import io.github.progark.Server.Service.LeaderboardService;
import io.github.progark.Client.Views.Game.LeaderboardView;
import io.github.progark.Server.database.DataCallback;

public class LeaderboardController extends Controller {
    private LeaderboardModel model;
    private LeaderboardView view;
    private LeaderboardService service;

    public LeaderboardController(LeaderboardModel model, LeaderboardView view, LeaderboardService service) {
        this.model = model;
        this.view = view;
        this.service = service;
    }

    @Override
    public void enter() {

        loadLeaderboard();
    }

    @Override
    public void update(float delta) { }

    @Override
    public void dispose() { }

    public void loadLeaderboard() {
        service.loadLeaderboard(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof LeaderboardModel) {
                    model = (LeaderboardModel) data;
                    if (view != null) {
                        view.updateLeaderboard(model);
                    }
                }
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void incrementScoreFor(String userId) {
        service.incrementUserScore(userId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {

                loadLeaderboard();
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void setLeaderBoardView(LeaderboardView view) {
        this.view = view;
    }

}
