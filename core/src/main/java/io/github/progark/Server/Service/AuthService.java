package io.github.progark.Server.Service;

import io.github.progark.Server.database.DataCallback;

public interface AuthService {

    void signUp(String email, String password, String username, DataCallback callback);
    void signIn(String email, String password, DataCallback dataCallback);
    void signOut();
    boolean isUserLoggedIn();
    String getCurrentUserEmail();
    String getCurrentUser(DataCallback callback);
    void getUsernameFromUserId(String userId, DataCallback callback);
    void getLoggedInUsername(DataCallback callback);

}
