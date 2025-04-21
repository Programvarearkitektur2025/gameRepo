package io.github.progark.Server.Service;

public interface DataCallback {
    void onSuccess(Object data);
    void onFailure(Exception e);
}
