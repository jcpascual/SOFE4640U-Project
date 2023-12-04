package net.uoit.sofe4640.group4.project.database;

public class AppUser {
    public int id;
    public String username;
    public String password;

    public AppUser()  {
        // Set defaults.
        // An ID of -1 is a special value indicating this AppUser has never been inserted into the database.
        id = -1;
        username = "";
        password = "";
    }
}
