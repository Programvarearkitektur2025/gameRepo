package io.github.progark.Server.database;


import java.util.Map;

public interface DatabaseManager {
    void writeData(String key, Object data); // instead of String json


    void readData(String key, DataCallback callback);
    //void deleteData(String key, dataCallback callback);
    void subscribeToDocument(String documentKey, DataCallback callback);


}
