package io.github.progark.Client.Controllers;

import java.sql.Timestamp;

import io.github.progark.Client.Model.LobbyModel;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.Service.LobbyService;
import io.github.progark.Client.Views.Game.LobbyView;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.database.DataCallback;

public class LobbyController {

    private final LobbyService lobbyService;
    private final LobbyModel lobbyModel;
    private LobbyView lobbyView;
    private final AuthService authService;

    public LobbyController(LobbyService lobbyService, LobbyModel lobbyModel, LobbyView lobbyView, AuthService authService) {
        this.lobbyService = lobbyService;
        this.lobbyModel = lobbyModel;
        this.lobbyView = lobbyView;
        this.authService = authService;
    }

    public void createLobby() {
        authService.getCurrentUser(new DataCallback() {
            @Override
            public void onSuccess(Object userObj) {
                UserModel user = (UserModel) userObj;

                lobbyService.createLobby(user.getUsername(), new DataCallback() {
                    @Override
                    public void onSuccess(Object codeObj) {
                        String lobbyCode = (String) codeObj;

                        // Update local LobbyModel
                        lobbyModel.setLobbyCode(lobbyCode);
                        lobbyModel.setPlayerOne(user.getUsername());
                        lobbyModel.setPlayerTwo(null);
                        lobbyModel.setStatus("waiting");
                        lobbyModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                        System.out.println("Lobby created: " + lobbyModel);

                        lobbyService.subscribeToLobbyUpdates(lobbyCode, new DataCallback() {
                            @Override
                            public void onSuccess(Object updatedLobbyObj) {
                                LobbyModel updatedLobby = (LobbyModel) updatedLobbyObj;

                                if ("full".equals(updatedLobby.getStatus())) {
                                    System.out.println("Lobby is full! Opponent joined: " + updatedLobby.getPlayerTwo());

                                    lobbyModel.setPlayerTwo(updatedLobby.getPlayerTwo());
                                    lobbyModel.setStatus("full");

                                    // lobbyView.startGame(lobbyModel.getPlayerOne(), lobbyModel.getPlayerTwo());
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.out.println("Failed to subscribe to lobby updates: " + e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Failed to create lobby: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to fetch user for lobby: " + e.getMessage());
            }
        });
    }


    public void joinLobby(String lobbyCode) {
        authService.getCurrentUser(new DataCallback() {
            @Override
            public void onSuccess(Object userObj) {
                UserModel user = (UserModel) userObj;

                lobbyService.joinLobby(lobbyCode, user.getUsername(), new DataCallback() {
                    @Override
                    public void onSuccess(Object updatedLobby) {
                        LobbyModel joinedLobby = (LobbyModel) updatedLobby;
                        lobbyModel.setLobbyCode(joinedLobby.getLobbyCode());
                        lobbyModel.setPlayerOne(joinedLobby.getPlayerOne());
                        lobbyModel.setPlayerTwo(joinedLobby.getPlayerTwo());
                        lobbyModel.setStatus(joinedLobby.getStatus());
                        lobbyModel.setCreatedAt(joinedLobby.getCreatedAt());

                        System.out.println("Successfully joined lobby: " + joinedLobby);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Failed to join lobby: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failed to fetch current user: " + e.getMessage());
            }
        });
    }

}
