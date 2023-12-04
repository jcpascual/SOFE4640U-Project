package net.uoit.sofe4640.group4.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.database.Scooter;
import net.uoit.sofe4640.group4.project.database.ScooterState;
import net.uoit.sofe4640.group4.project.databinding.ActivityLoginBinding;
import net.uoit.sofe4640.group4.project.databinding.ActivityScooterDetailBinding;

import java.util.Collections;
import java.util.Comparator;

public class ScooterDetailActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityScooterDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScooterDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarScooterDetail);

        getSupportActionBar().setTitle(R.string.scooter_detail_title);

        // Get the ProjectApplication instance.
        ProjectApplication application = (ProjectApplication)getApplication();

        // Grab the database helper from our ProjectApplication instance.
        AppDatabaseHelper dbHelper = application.getDatabaseHelper();

        // Get the Scooter's ID.
        Intent intent = getIntent();
        int scooterId = intent.getIntExtra("scooterId", -1);

        // Get the Scooter instance from the database.
        Scooter scooter = dbHelper.getScooter(scooterId);

        // Set the address on the TextView.
        binding.textViewLocation.setText(scooter.address);

        binding.mapViewDetail.onCreate(savedInstanceState);

        // Load the map.
        binding.mapViewDetail.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng latLng = new LatLng(scooter.parkedLatitude, scooter.parkedLongitude);

                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Scooter"));

                // Move the camera to show the bounds.
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);
                googleMap.moveCamera(cameraUpdate);
            }
        });

        binding.buttonRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the scooter as rented by our user.
                scooter.state = ScooterState.RENTED;
                scooter.rentingUser = application.getCurrentUser().id;

                // Update the Scooter in the database.
                dbHelper.addOrUpdateScooter(scooter);

                // Finish this activity.
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.mapViewDetail.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.mapViewDetail.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        binding.mapViewDetail.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        binding.mapViewDetail.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding.mapViewDetail.onDestroy();

        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        binding.mapViewDetail.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        binding.mapViewDetail.onLowMemory();
    }
}