package io.github.progark.Server.database.GameInstance;

import java.util.Map;

import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class GameInstance implements DatabaseManager {

    private DatabaseManager db;


    /*@Override
    public void writeData(String key, String value) {

    } */

    @Override
    public void writeData(String key, Map<String, Object> value) {

    }

    @Override
    public void readData(String key, DataCallback callback) {

    }

    /*
    @Override
    public void deleteData(String key, dataCallback callback) {

    }
    */
}
