package io.github.progark.Server.database;

public interface dataCallback {
    void onSuccess(String value);
    void onFailure(Exception e);
}
