package io.github.progark.Server.Service;

import io.github.progark.Server.database.DataCallback;
/*
 * AuthService.java
 * This interface defines the methods for user authentication and management.
 * It provides methods for signing up, signing in, signing out,
 * checking if a user is logged in, and retrieving user information.
 * The AuthService interface is typically implemented by a class that handles
 * the actual authentication logic, such as interacting with a database or an authentication service.
 * The interface is designed to be flexible and can be adapted to different authentication systems.
 * The methods in this interface are asynchronous and use callbacks to handle success and failure scenarios.
 */
public interface AuthService {

    void signUp(String email, String password, String username, DataCallback callback);
    void signIn(String email, String password, DataCallback dataCallback);
    void signOut();
    boolean isUserLoggedIn();

    /*
    Function for getting the current user
     */
    String getCurrentUserId();

    String getCurrentUserEmail();
    String getCurrentUser(DataCallback callback);
    void getUsernameFromUserId(String userId, DataCallback callback);
    void getLoggedInUsername(DataCallback callback);

}
