package net.uoit.sofe4640.group4.project;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.database.Scooter;
import net.uoit.sofe4640.group4.project.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build();

                GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(view.getContext(), options);

                scanner.startScan()
                        .addOnSuccessListener(
                                barcode -> {
                                    String value = barcode.getRawValue();

                                    // We want a QR code whose contents start with "scooter".
                                    if (!value.startsWith("scooter")) {
                                        showErrorAlert(R.string.error_qr_bad_format_text);
                                    } else {
                                        // Get the scooter ID.
                                        String[] splitValue = value.split(":");
                                        int scooterId = Integer.valueOf(splitValue[1]);

                                        // Get the database helper.
                                        ProjectApplication projectApplication = (ProjectApplication)getApplication();
                                        AppDatabaseHelper dbHelper = projectApplication.getDatabaseHelper();

                                        // Get the scooter with the ID.
                                        Scooter scooter = dbHelper.getScooter(scooterId);

                                        if (scooter == null) {
                                            showErrorAlert(R.string.error_qr_bad_id_text);
                                        } else {
                                            // Open the detail view with this scooter.
                                            Intent intent = new Intent(MainActivity.this, ScooterDetailActivity.class);
                                            intent.putExtra("scooterId", scooter.id);

                                            startActivity(intent);
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                e -> {
                                    Log.e("MainActivity", "Failed to open QR code scanner.", e);

                                    showErrorAlert(R.string.error_qr_scanner_fail_text);
                                });
                ;
            }
        });

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION_PERMISSIONS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_LOCATION_PERMISSIONS) {
            return;
        }

        if (grantResults.length == 2) {
            return;
        }

        showErrorAlert(R.string.error_location_permissions_denied_text);
    }

    private void showErrorAlert(int messageResId) {
        // Build an AlertDialog with the error information.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_common_title)
                .setMessage(messageResId)
                .setPositiveButton(R.string.error_common_button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.show();
    }
}