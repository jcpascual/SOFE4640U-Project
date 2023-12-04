package net.uoit.sofe4640.group4.project.database;

public class Scooter {
    public int id;
    public ScooterState state;
    public double parkedLatitude;
    public double parkedLongitude;
    public int rentingUser;

    // Not part of the DB, just helper variables for the scooter list.
    public String address;
    public double distance;

    public Scooter()  {
        // Set defaults.
        // An ID of -1 is a special value indicating this Scooter has never been inserted into the database.
        id = -1;
        state = ScooterState.AVAILABLE;
        parkedLatitude = 0;
        parkedLongitude = 0;
        rentingUser = -1;
        address = "";
        distance = -1.0d;
    }
}
