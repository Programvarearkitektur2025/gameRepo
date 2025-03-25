package io.github.progark.Client.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;
public class ResultService {
    private final DatabaseManager databaseManager;
    public ResultService (DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }
}
