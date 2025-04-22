package io.github.progark.android;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;
/*
 * AndroidDatabaseManager.java
 * This class is responsible for managing the database operations using Firebase Firestore.
 * It implements the DatabaseManager interface and provides methods for writing, reading,
 * and subscribing to data in the Firestore database.
 * It also provides a singleton instance for easy access throughout the application.
 * The class is designed to be used in an Android environment and handles the database operations
 * asynchronously using callbacks.
 * The class is thread-safe and ensures that only one instance of the database manager is created.
 */
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
/*
* Function for writing data to firebase
 */
    @Override
    public void writeData(String key, Object data) {
        firestore.document(key)
            .set(data)
            .addOnSuccessListener(aVoid -> System.out.println("Firestore: Data saved successfully"))
            .addOnFailureListener(e -> System.out.println("Firestore: Data save failed: " + e.getMessage()));
    }

/*
* Function to read data from firebase
 */
    @Override
    public void readData(String key, DataCallback callback) {
        if (key.contains("/")) { // It's a document
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
        } else { // It's a collection
            firestore.collection(key)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        List<Map<String, Object>> documents = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            documents.add(document.getData()); // Get each document's data
                        }
                        callback.onSuccess(documents);
                    } else {
                        callback.onFailure(new Exception("Collection is empty"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
        }
    }

/*
* Function which was meant to be used for Pub-Sub pattern
 */
    @Override
    public void subscribeToDocument(String key, DataCallback callback) {
        firestore.document(key).addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                callback.onFailure(error);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                String json = snapshot.getData().toString();
                callback.onSuccess(json);
            } else {
                callback.onFailure(new Exception("No data found"));
            }
        });
    }

    /*
    * Function used for writing questions to firebase
     */
    public void writeQuestion(String collectionPath, Map<String, Object> data) {
        FirebaseFirestore.getInstance()
            .collection(collectionPath)
            .add(data)
            .addOnSuccessListener(documentReference ->
                System.out.println("Added to " + collectionPath + ": " + documentReference.getId()))
            .addOnFailureListener(e ->
                System.err.println("Failed to write data: " + e.getMessage()));
    }


}
