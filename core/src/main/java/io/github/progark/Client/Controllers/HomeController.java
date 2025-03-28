package io.github.progark.Client.Controllers;

import io.github.progark.Server.Model.Game.HomeModel;
import io.github.progark.Server.Service.HomeService;
import io.github.progark.Client.Views.Menu.HomeView;
import io.github.progark.Server.database.DataCallback;

public class HomeController {
    private HomeModel homeModel;
    private HomeService homeService;
    private HomeView homeView;

    public HomeController(HomeService homeService, HomeModel homeModel) {
        this.homeService = homeService;
        this.homeModel = homeModel;
    }

    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    public void loadUserGames(String userId) {
        homeService.loadUserGames(userId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof HomeModel) {
                    System.out.println(data);
                    homeModel = (HomeModel) data;
                    if (homeView != null) {
                        homeView.updateGameLists(homeModel);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to load games: " + e.getMessage());
            }
        });
    }

    public void createNewGame(String userId, String opponentId) {
        homeService.createNewGame(userId, opponentId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String gameId = (String) data;
                System.out.println("Created new game with ID: " + gameId);
                loadUserGames(userId); // Reload games list
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to create game: " + e.getMessage());
            }
        });
    }

    public void joinGame(String userId, String gameId) {
        homeService.joinGame(userId, gameId, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String joinedGameId = (String) data;
                System.out.println("Joined game with ID: " + joinedGameId);
                loadUserGames(userId); // Reload games list
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to join game: " + e.getMessage());
            }
        });
    }

    public void handleGameEntryClick(String gameId) {
        // Handle game entry click - navigate to game view
        if (homeView != null) {
            homeView.navigateToGame(gameId);
        }
    }
}
