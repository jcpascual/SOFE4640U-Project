package net.uoit.sofe4640.group4.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.database.Scooter;
import net.uoit.sofe4640.group4.project.database.ScooterState;
import net.uoit.sofe4640.group4.project.databinding.ActivityScooterDetailBinding;
import net.uoit.sofe4640.group4.project.databinding.ActivityScooterRentedDetailBinding;

public class ScooterRentedDetailActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityScooterRentedDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScooterRentedDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarScooterRentedDetail);

        getSupportActionBar().setTitle(R.string.scooter_rented_detail_title);

        // Get the ProjectApplication instance.
        ProjectApplication application = (ProjectApplication)getApplication();

        // Grab the database helper from our ProjectApplication instance.
        AppDatabaseHelper dbHelper = application.getDatabaseHelper();

        // Get the current rented Scooter.
        Scooter scooter = dbHelper.getRentedScooter(application.getCurrentUser());

        // If there is no rented Scooter, exit.
        if (scooter == null) {
            finish();
        }

        // Register card click listeners that open videos.

        binding.cardHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(ScooterRentedDetailActivity.this, YouTubeVideoActivity.class);
                // intent.putExtra("videoId", "im1m106TBYU");

                Intent intent = new Intent(ScooterRentedDetailActivity.this, LocalVideoActivity.class);
                intent.putExtra("resId", R.raw.howto);

                startActivity(intent);
            }
        });

        binding.cardSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(ScooterRentedDetailActivity.this, YouTubeVideoActivity.class);
                // intent.putExtra("videoId", "5CBxr3s9nqM");

                Intent intent = new Intent(ScooterRentedDetailActivity.this, LocalVideoActivity.class);
                intent.putExtra("resId", R.raw.safety);

                startActivity(intent);
            }
        });

        binding.cardTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(ScooterRentedDetailActivity.this, YouTubeVideoActivity.class);
                // intent.putExtra("videoId", "cCA4euH0jnc");

                Intent intent = new Intent(ScooterRentedDetailActivity.this, LocalVideoActivity.class);
                intent.putExtra("resId", R.raw.tips);

                startActivity(intent);
            }
        });

        // If the finish button is pressed, unset the Scooter as rented in the database.
        binding.buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scooter.state = ScooterState.AVAILABLE;
                scooter.rentingUser = -1;

                dbHelper.addOrUpdateScooter(scooter);

                finish();
            }
        });
    }
}