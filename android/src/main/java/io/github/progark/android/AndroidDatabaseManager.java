package io.github.progark.android;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class AndroidDatabaseManager implements DatabaseManager {
    private static AndroidDatabaseManager instance;
    private final FirebaseFirestore firestore;

    private AndroidDatabaseManager() {
        firestore = FirebaseFirestore.getInstance();
    }

    public static synchronized AndroidDatabaseManager getInstance() {
        if (instance == null) {
            instance = new AndroidDatabaseManager();
        }
        return instance;
    }

    @Override
    public void writeData(String key, Map<String, Object> value) {
        firestore.document(key)
            .set(value)
            .addOnSuccessListener(aVoid -> System.out.println("Firestore: Data saved successfully"))
            .addOnFailureListener(e -> System.out.println("Firestore: Data save failed: " + e.getMessage()));
    }

    @Override
    public void readData(String key, DataCallback callback) {
        firestore.document(key)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    callback.onSuccess(documentSnapshot.getData());
                } else {
                    callback.onFailure(new Exception("Document not found"));
                }
            })
            .addOnFailureListener(callback::onFailure);
    }
}
