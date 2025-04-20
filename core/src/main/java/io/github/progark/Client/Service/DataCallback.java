package io.github.progark.Client.Service;

public interface DataCallback {
    void onSuccess(Object data);
    void onFailure(Exception e);
} 