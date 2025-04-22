package io.github.progark.Client.Controllers;

import java.util.HashMap;
import java.util.Map;

import io.github.progark.Client.Views.Game.LeaderBoardView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.LeaderboardModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.LeaderboardService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;
/*
 * LeaderboardController.java
 * This class is responsible for managing the leaderboard functionality.
 * It handles loading the leaderboard data, updating scores, and interacting with the leaderboard service.
 * It also manages the view for displaying the leaderboard.
 * The controller interacts with the LeaderboardService to perform operations related to the leaderboard.
 */
public class LeaderboardController extends Controller {

    private final LeaderBoardView view;
    private final LeaderboardService leaderboardService;
    private final Main main;
    private LeaderboardModel leaderboardModel;
    private AuthService authService;

    public LeaderboardController(DatabaseManager databaseManager, AuthService authService, Main main) {
        this.main = main;
        this.leaderboardService = new LeaderboardService(databaseManager, authService);
        this.view = new LeaderBoardView(this);
        this.authService = authService;
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

    /*
     * loadLeaderboard
     * This method is responsible for loading the leaderboard data.
     * It retrieves the leaderboard data from the leaderboard service and updates the leaderboard model.
     * It also sorts the leaderboard data in descending order based on user scores.
     */
    public void loadLeaderboard(DataCallback dataCallback) {
        leaderboardService.loadLeaderboard(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof LeaderboardModel) {
                    leaderboardModel = (LeaderboardModel) data;
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
/*
 * incrementScoreFor
 * This method is responsible for incrementing the score for a specific user.
 * It uses the leaderboard service to update the user's score in the database.
 */
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
    /*
     * loadLoggedInUserScore
     * This method is responsible for loading the score of the logged-in user.
     * It retrieves the username of the logged-in user and then fetches the user's score from the leaderboard service.
     */
    public void loadLoggedInUserScore(String username) {
        leaderboardService.getUserLeaderboardScore(username, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof Integer) {
                    int userScore = (Integer) data;
                    System.out.println("User " + username + " has score: " + userScore);
                }
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


    public void loadUsername(String userId) {
        authService.getUsernameFromUserId(userId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof String) {
                    String username = (String) data;
                    System.out.println("Brukernavn for userId=" + userId + " er: " + username);

                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadOwnScore(DataCallback callback) {
        authService.getLoggedInUsername(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof String) {
                    String myUsername = (String) data;
                    leaderboardService.getUserLeaderboardScore(myUsername, new DataCallback() {
                        @Override
                        public void onSuccess(Object scoreObj) {
                            if (scoreObj instanceof Integer) {
                                int score = (Integer) scoreObj;
                                Map<String,Object> result = new HashMap<>();
                                result.put("username", myUsername);
                                result.put("score", score);
                                callback.onSuccess(result);
                            }
                        }
                        @Override
                        public void onFailure(Exception e) {
                            callback.onFailure(e);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }



}
