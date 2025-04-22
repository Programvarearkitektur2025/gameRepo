package io.github.progark.Server.database;
/*
 * DataCallback.java
 * This interface defines the callback methods for handling data operations.
 * It provides methods for success and failure scenarios.
 * The onSuccess method is called when the data operation is successful,
 * and the onFailure method is called when there is an error.
 * The DataCallback interface is used to handle asynchronous operations
 * and allows the caller to define custom behavior for success and failure cases.
 */
public interface DataCallback {
    void onSuccess(Object data);
    void onFailure(Exception e);
}
