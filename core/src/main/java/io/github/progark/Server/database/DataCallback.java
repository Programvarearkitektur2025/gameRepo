package io.github.progark.Server.database;

public interface DataCallback {
    void onSuccess(Object data);
    void onFailure(Exception e);
}
