package net.uoit.sofe4640.group4.project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Geocoder;

import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "project_app";
    private static final int DATABASE_SCHEMA_VERSION = 2;


    private static final String USER_TABLE_NAME = "users";
    private static final String SCOOTER_TABLE_NAME = "scooters";


    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the user table with three columns: the ID (auto incrementing), their username, and their password.
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);");

        // Create the scooter table with five columns: the ID (auto incrementing), its state, its latitude, its longitude, and the current renting user.
        db.execSQL("CREATE TABLE " + SCOOTER_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, state TEXT, parkedLatitude REAL, parkedLongitude REAL, address TEXT, rentingUser ID);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prev, int next) {
        // We don't migrate anything.
        throw new RuntimeException("Migration not supported");
    }

    // WARNING: this method drops all data in the database!
    public void importDebugData(SQLiteDatabase db) {
        if (db == null) {
            db = getWritableDatabase();
        }

        try {
            db.execSQL("DROP TABLE " + USER_TABLE_NAME + ";");
            db.execSQL("DROP TABLE " + SCOOTER_TABLE_NAME + ";");
        } catch (SQLException e) {
            // Do nothing, this is because the tables don't exist already.
        }

        onCreate(db);

        // Add a test user.

        AppUser user = new AppUser();
        user.username = "testuser";
        user.password = "testpassword";

        addOrUpdateAppUser(user);

        // Add dummy Scooters.

        Scooter scooter = new Scooter();
        scooter.state = ScooterState.AVAILABLE;
        scooter.parkedLatitude = 43.94546;
        scooter.parkedLongitude = -78.89256;

        addOrUpdateScooter(scooter);

        Scooter scooter2 = new Scooter();
        scooter2.state = ScooterState.AVAILABLE;
        scooter2.parkedLatitude = 43.94440;
        scooter2.parkedLongitude = -78.89096;

        addOrUpdateScooter(scooter2);

        Scooter scooter3 = new Scooter();
        scooter3.state = ScooterState.AVAILABLE;
        scooter3.parkedLatitude = 43.95598;
        scooter3.parkedLongitude = -78.90014;

        addOrUpdateScooter(scooter3);

        Scooter scooter4 = new Scooter();
        scooter4.state = ScooterState.AVAILABLE;
        scooter4.parkedLatitude = 43.93936;
        scooter4.parkedLongitude = -78.88370;

        addOrUpdateScooter(scooter4);

        Scooter scooter5 = new Scooter();
        scooter5.state = ScooterState.AVAILABLE;
        scooter5.parkedLatitude = 43.94581;
        scooter5.parkedLongitude = -78.90593;

        addOrUpdateScooter(scooter5);

        Scooter scooter6 = new Scooter();
        scooter6.state = ScooterState.AVAILABLE;
        scooter6.parkedLatitude = 43.95234;
        scooter6.parkedLongitude = -78.87673;

        addOrUpdateScooter(scooter6);
    }

    /*

    Users

     */

    public void addOrUpdateAppUser(AppUser user) {
        SQLiteDatabase db = getWritableDatabase();

        // Create the ContentValues.
        ContentValues values = new ContentValues();
        values.put("username", user.username);
        values.put("password", user.password);

        // If the ID is not -1, then this AppUser has already been inserted into the database.
        if (user.id != -1) {
            values.put("id", user.id);

            // Update the existing row.
            db.update(USER_TABLE_NAME, values, "id = ?", new String[] { String.valueOf(user.id) });
        } else {
            values.putNull("id");

            // Insert a new row into the database.
            db.insert(USER_TABLE_NAME, null, values);
        }

        db.close();
    }

    private AppUser getAppUserFromCursor(Cursor cursor) {
        // Construct the Scooter instance using the row data.
        AppUser user = new AppUser();
        user.id = cursor.getInt(0);
        user.username = cursor.getString(1);
        user.password = cursor.getString(2);

        return user;
    }

    public AppUser getAppUserWithUsernameAndPassword(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        // Query the database for the row with the specified ID.
        Cursor cursor = db.query(USER_TABLE_NAME, new String[] { "id", "username", "password" }, "username = ? AND password = ?", new String[] { username, password }, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        // Read the data.
        AppUser user = getAppUserFromCursor(cursor);

        cursor.close();
        db.close();

        return user;
    }

    /*

    ========
    Scooters
    ========

     */

    public void addOrUpdateScooter(Scooter scooter) {
        SQLiteDatabase db = getWritableDatabase();

        // Create the ContentValues.
        ContentValues values = new ContentValues();
        values.put("state", scooter.state.toString());
        values.put("parkedLatitude", scooter.parkedLatitude);
        values.put("parkedLongitude", scooter.parkedLongitude);
        values.put("address", scooter.address);
        values.put("rentingUser", scooter.rentingUser);

        // If the ID is not -1, then this Scooter has already been inserted into the database.
        if (scooter.id != -1) {
            values.put("id", scooter.id);

            // Update the existing row.
            db.update(SCOOTER_TABLE_NAME, values, "id = ?", new String[] { String.valueOf(scooter.id) });
        } else {
            values.putNull("id");

            // Insert a new row into the database.
            db.insert(SCOOTER_TABLE_NAME, null, values);
        }

        db.close();
    }

    private Scooter getScooterFromCursor(Cursor cursor) {
        // Construct the Scooter instance using the row data.
        Scooter scooter = new Scooter();
        scooter.id = cursor.getInt(0);
        scooter.state = ScooterState.valueOf(cursor.getString(1));
        scooter.parkedLatitude = cursor.getDouble(2);
        scooter.parkedLongitude = cursor.getDouble(3);
        scooter.address = cursor.getString(4);
        scooter.rentingUser = cursor.getInt(5);

        return scooter;
    }

    public Scooter getScooter(int id) {
        SQLiteDatabase db = getReadableDatabase();

        // Query the database for the row with the specified ID.
        Cursor cursor = db.query(SCOOTER_TABLE_NAME, new String[] { "id", "state", "parkedLatitude", "parkedLongitude", "address", "rentingUser" }, "id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();

        // Read the data.
        Scooter scooter = getScooterFromCursor(cursor);

        cursor.close();
        db.close();

        return scooter;
    }

    public Scooter getRentedScooter(AppUser user) {
        SQLiteDatabase db = getReadableDatabase();

        // Query the database for the row with the rented scooter.
        Cursor cursor = db.query(SCOOTER_TABLE_NAME, new String[] { "id", "state", "parkedLatitude", "parkedLongitude", "address", "rentingUser" }, "rentingUser = ?", new String[] { String.valueOf(user.id) }, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        // Read the data.
        Scooter scooter = getScooterFromCursor(cursor);

        cursor.close();
        db.close();

        return scooter;
    }

    public List<Scooter> getScooters() {
        SQLiteDatabase db = getReadableDatabase();

        // Select every row from the database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + SCOOTER_TABLE_NAME, null);

        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();

        ArrayList<Scooter> scooters = new ArrayList<>();

        // Create Scooter instances using the row data.
        while (!cursor.isAfterLast()) {
            Scooter scooter = getScooterFromCursor(cursor);
            scooters.add(scooter);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return scooters;
    }
}
