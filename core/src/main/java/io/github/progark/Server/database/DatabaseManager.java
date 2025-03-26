package io.github.progark.Server.database;


import java.util.Map;

public interface DatabaseManager {
    void writeData(String key, Map<String, Object> value);

    void readData(String key, DataCallback callback);
    //void deleteData(String key, dataCallback callback);

}
