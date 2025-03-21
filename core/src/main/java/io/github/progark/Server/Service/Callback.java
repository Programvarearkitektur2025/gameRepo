package io.github.progark.Server.Service;


// Interface used for asynchronous functions
public interface Callback{
    void onSuccess(String message);
    void onFailure(Exception e);
}
