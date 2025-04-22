package io.github.progark.Server.Model.Login;
/*
 * UserModel.java
 * This class represents a user in the application.
 * It contains the user's unique identifier (uid), email, and username.
 * The class provides methods to get and set these attributes.
 * The UserModel class is used to manage user data and is typically used in conjunction with the UserController and UserView classes.
 */
public class UserModel {

    private String uid;
    private String email;
    private String username;

    public UserModel(String uid, String email, String username) {
        this.uid = uid;
        this.email = email;
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserModel{" +
            "uid='" + uid + '\'' +
            ", email='" + email + '\'' +
            ", username='" + username + '\'' +
            '}';
    }
}
