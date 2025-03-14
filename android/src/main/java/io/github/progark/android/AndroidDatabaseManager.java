package io.github.progark.android;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.progark.Server.database.dataCallback;
import io.github.progark.Server.database.databaseManager;

public class AndroidDatabaseManager implements databaseManager{
    private static AndroidDatabaseManager instance;
    private DatabaseReference database;

    private AndroidDatabaseManager(){
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        this.database = databaseInstance.getReference();
    }

    public static synchronized AndroidDatabaseManager getInstance() {
        if (instance == null) {
            instance = new AndroidDatabaseManager();
        }
        return instance;
    }

    @Override
    public void writeData(String key, String value) {
        database.child("gameData").child(key).setValue(value)
            .addOnSuccessListener(aVoid -> System.out.println("Firebase: " + "Data saved successfully"))
            .addOnFailureListener(e -> System.out.println("Firebase: " + "Data save not successful"));
    }

    @Override
    public void readData(String key, dataCallback callback) {
        database.child("gameData").child(key).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getValue() != null) {
                callback.onSuccess(task.getResult().getValue().toString());
            } else {
                callback.onFailure(new Exception("Failed to read data"));
            }
        });
    }



}
