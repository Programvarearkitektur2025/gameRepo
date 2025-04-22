package io.github.progark.Server.database;


import java.util.Map;
/*
 * DatabaseManager.java
 * This interface defines the methods for interacting with the database.
 * It provides methods for writing, reading, and subscribing to data in the database.
 * It also includes methods for writing questions and managing game data.
 * The implementation of this interface will handle the actual database operations.
 * The interface is designed to be flexible and can be adapted to different database systems.
 */
public interface DatabaseManager {
    void writeData(String key, Object data);


    void readData(String key, DataCallback callback);
    //void deleteData(String key, dataCallback callback);
    void subscribeToDocument(String documentKey, DataCallback callback);

    void writeQuestion(String collectionPath, Map<String, Object> data);

}
