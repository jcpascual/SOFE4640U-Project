package net.uoit.sofe4640.group4.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.database.Scooter;
import net.uoit.sofe4640.group4.project.databinding.FragmentScooterListBinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScooterListFragment extends Fragment {

    private FragmentScooterListBinding binding;

    private List<Scooter> heldScooters;
    private ScooterAdapter scooterAdapter;

    private Marker locationMarker;
    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentScooterListBinding.inflate(inflater, container, false);

        // Grab the helper from our LabApplication instance.
        AppDatabaseHelper helper = ((ProjectApplication)getActivity().getApplication()).getDatabaseHelper();

        heldScooters = helper.getScooters();

        // Create a new ScooterAdapter.
        scooterAdapter = new ScooterAdapter(heldScooters, new ScooterAdapter.OnScooterSelectedListener() {
            @Override
            public void onScooterSelect(int id) {
                Intent intent = new Intent(getContext(), ScooterDetailActivity.class);
                intent.putExtra("scooterId", id);

                startActivity(intent);
            }
        });

        // Set up the RecyclerView.
        binding.recyclerView.setAdapter(scooterAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.mapView.onCreate(savedInstanceState);

        // Fetch the map.
        binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Start building a LatLngBounds.
                LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

                // Add a Marker for each Scooter, and add it to the bounds.
                for (Scooter scooter : heldScooters) {
                    LatLng position = new LatLng(scooter.parkedLatitude, scooter.parkedLongitude);

                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(scooter.address));

                    boundsBuilder.include(position);
                }

                // Build the bounds.
                LatLngBounds bounds = boundsBuilder.build();

                // Move the camera to show the bounds.
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                googleMap.moveCamera(cameraUpdate);

                // Check if we have permission.
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Show the device's current location.
                    googleMap.setMyLocationEnabled(true);

                    // Receive updates whenever the location changes.
                    googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(@NonNull Location location) {
                            // Calculate the new distance of each Scooter.
                            for (Scooter scooter : heldScooters) {
                                float[] result = new float[1];
                                Location.distanceBetween(location.getLatitude(), location.getLongitude(), scooter.parkedLatitude, scooter.parkedLongitude, result);

                                scooter.distance = result[0];
                            }

                            // Sort the Scooters by distance.
                            Collections.sort(heldScooters, new Comparator<Scooter>() {
                                @Override
                                public int compare(Scooter o1, Scooter o2) {
                                    return Double.compare(o1.distance, o2.distance);
                                }
                            });

                            // Update the adapter.
                            scooterAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.mapView.onResume();

        ProjectApplication application = (ProjectApplication)getActivity().getApplication();

        // Grab the helper from our ProjectApplication instance.
        AppDatabaseHelper helper = application.getDatabaseHelper();

        // Check if we are renting a scooter.
        if (helper.getRentedScooter(application.getCurrentUser()) != null) {
            // Open the ScooterRentedDetailActivity.
            Intent intent = new Intent(getContext(), ScooterRentedDetailActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        binding.mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        binding.mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding.mapView.onDestroy();

        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        binding.mapView.onLowMemory();
    }
}