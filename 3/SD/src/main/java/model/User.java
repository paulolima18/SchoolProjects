package model;

/**
 * User class defines a user for the App.
 * @author Group_X
 */
public class User {
    private String name;
    private String username;
    private String password;

    public User() {
        this.name ="";
        this.username="";
        this.password="";
    }

    public User(String username, String password, String name)
    {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public boolean login(String in_username, String in_password)
    {
        return (this.username.equals(in_username) && this.password.equals(in_password));
    }

    public boolean verifyCredentials(String Password) {

        return this.password.equals(Password);
    }

    public String getName() {
        return this.name;
    }
    public String getUsername() { return this.username;}
    public String getPassword() { return this.password;}

    public User clone(){
        return new User(this.name, this.username, this.password);
    }
}
