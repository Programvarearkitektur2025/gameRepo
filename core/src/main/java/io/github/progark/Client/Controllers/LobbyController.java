package io.github.progark.Client.Controllers;

import java.sql.Timestamp;

import io.github.progark.Client.Model.LobbyModel;
import io.github.progark.Client.Service.LobbyService;
import io.github.progark.Client.Views.Game.LobbyView;
import io.github.progark.Server.database.DataCallback;

public class LobbyController {


    private LobbyService lobbyService;
    private LobbyModel lobbyModel;
    private LobbyView lobbyView;

    public LobbyController(LobbyService lobbyService, LobbyModel lobbyModel, LobbyView lobbyView) {
        this.lobbyService = lobbyService;
        this.lobbyModel = lobbyModel;
        this.lobbyView = lobbyView;
    }


    public void createLobby() {
        lobbyService.createLobby("TestUsername", new DataCallback() {
            @Override
            public void onSuccess(Object code) {
                String lobbyCode = (String) code;

                lobbyModel.setLobbyCode(lobbyCode);
                lobbyModel.setPlayerOne("TestUsername");
                lobbyModel.setPlayerTwo(null);
                lobbyModel.setStatus("waiting");
                lobbyModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));




            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e);
            }
        });

        //Need to add the joinlobby and publish & subscribe methods
    }

}
