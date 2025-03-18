package io.github.progark.Server.Service;


// Interface used for asynchronous functions
public interface SignInCallback{
    void onSuccess(String message);
    void onFailure(Exception e);
}
