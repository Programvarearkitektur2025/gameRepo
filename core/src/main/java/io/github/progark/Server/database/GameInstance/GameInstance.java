package io.github.progark.Server.database.GameInstance;

import io.github.progark.Server.database.dataCallback;
import io.github.progark.Server.database.databaseManager;

public class GameInstance implements databaseManager{

    private databaseManager db;


    @Override
    public void writeData(String key, String value) {

    }

    @Override
    public void readData(String key, dataCallback callback) {

    }

    /*
    @Override
    public void deleteData(String key, dataCallback callback) {

    }
    */
}
