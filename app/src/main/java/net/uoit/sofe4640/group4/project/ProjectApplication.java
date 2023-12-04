package net.uoit.sofe4640.group4.project;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.database.AppUser;
import net.uoit.sofe4640.group4.project.database.Scooter;

import java.io.IOException;
import java.util.List;

public class ProjectApplication extends Application {
    // Our database helper. We store it here as the Application instance can be accessed from all Fragments.
    private AppDatabaseHelper databaseHelper;

    // The currently logged in user.
    private AppUser currentUser;

    @Override
    public void onCreate() {
        super.onCreate();

        databaseHelper = new AppDatabaseHelper(this);
        currentUser = null;

        databaseHelper.importDebugData(null);

        // Initialize the maps SDK.
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, new OnMapsSdkInitializedCallback() {
            @Override
            public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
                //
            }
        });

        try {
            // Get the addresses of all Scooter instances.
            Geocoder geocoder = new Geocoder(this);

            for (Scooter scooter : databaseHelper.getScooters()) {
                if (!scooter.address.equals("")) {
                    return;
                }

                // Look up the address for these coordinates.
                List<Address> addresses = geocoder.getFromLocation(scooter.parkedLatitude, scooter.parkedLongitude, 1);

                // Check if the look up succeeded.
                if (addresses.size() < 1) {
                    Log.e("ProjectApplication", "failed to look up address for scooter " + scooter.id);
                    continue;
                }

                // Get the returned Address instance.
                Address address = addresses.get(0);

                // Set it in the Scooter instance. We don't include the portion with city, province, etc.
                scooter.address = address.getAddressLine(0).split(",")[0];
                databaseHelper.addOrUpdateScooter(scooter);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AppDatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public AppUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(AppUser user) {
        currentUser = user;
    }
}
