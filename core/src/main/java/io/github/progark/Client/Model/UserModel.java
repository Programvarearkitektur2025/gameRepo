package io.github.progark.Client.Model;

public class UserModel {

    private String uid;
    private String email;
    private String username;

    public UserModel(String uid, String email, String username) {
        this.uid = uid;
        this.email = email;
        this.username = username;
    }

    public UserModel() {

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
