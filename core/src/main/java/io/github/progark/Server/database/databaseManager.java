package io.github.progark.Server.database;


public interface databaseManager {
    void writeData(String key, String value);
    void readData(String key, dataCallback callback);
    void deleteData(String key, dataCallback callback);
}
