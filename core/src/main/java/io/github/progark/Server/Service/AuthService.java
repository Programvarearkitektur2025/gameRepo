package io.github.progark.Server.Service;

public interface AuthService {

    void signUp(String email, String password);
    void signIn(String email, String password, Callback callback);
    void signOut();
    boolean isUserLoggedIn();
    String getCurrentUserEmail();
}
