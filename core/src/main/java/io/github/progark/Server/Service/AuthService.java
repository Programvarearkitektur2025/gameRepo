package io.github.progark.Server.Service;

import io.github.progark.Server.database.DataCallback;

public interface AuthService {

    void signUp(String email, String password);
    void signIn(String email, String password, DataCallback dataCallback);
    void signOut();
    boolean isUserLoggedIn();
    String getCurrentUserEmail();
}
