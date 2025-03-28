package io.github.progark.Server.Service;

import io.github.progark.Server.Model.Game.LobbyModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

// This class should only update the database once the results are final
public class GameService {
    //private final DatabaseManager databaseManager;

    private DatabaseManager databaseManager;
    private LobbyModel lobbyData;

    public GameService() {
        if (databaseManager != null){
            //Do nothing
        }
        else {
            databaseManager = new DatabaseManager();
            this.databaseManager = databaseManager;
        }


    }

    // Function for fetching lobby information asynchronuous.
    private void fetchLobbyInfo(String id) {
        databaseManager.readData("lobbies/" + id, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof LobbyModel) {
                    lobbyData = (LobbyModel) data;
                    System.out.println("Lobby data initialized: " + lobbyData);
                } else {
                    System.err.println("Invalid data type received for lobby");
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Failed to fetch lobby: " + e.getMessage());
            }
        });
    }

    public LobbyModel getLobbyData() {
        return lobbyData;
    }
}
