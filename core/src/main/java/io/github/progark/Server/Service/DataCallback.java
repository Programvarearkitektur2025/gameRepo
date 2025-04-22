package io.github.progark.Server.Service;
/*
 * DataCallback.java
 * This interface defines the callback methods for handling data operations.
 * It provides methods for success and failure scenarios.
 */
public interface DataCallback {
    void onSuccess(Object data);
    void onFailure(Exception e);
}
