package net.uoit.sofe4640.group4.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.databinding.ActivityLocalVideoBinding;

import java.io.IOException;

public class LocalVideoActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityLocalVideoBinding binding;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLocalVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarLocalVideo);

        getSupportActionBar().setTitle(R.string.local_video_title);

        // Get the asset from the Intent.
        Intent intent = getIntent();
        int resId = intent.getIntExtra("resId", -1);

        // If there's no resource ID, just exit the activity immediately.
        if (resId == -1) {
            finish();
        }

        binding.buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the player if it doesn't exist.
                if (player == null) {
                    player = MediaPlayer.create(LocalVideoActivity.this, resId);
                    player.setDisplay(binding.surfaceView.getHolder());
                    player.setLooping(true);
                }

                // Switch between playing or paused as needed.
                if (player.isPlaying()) {
                    binding.buttonControl.setText(R.string.local_video_button_play);
                    player.stop();
                } else {
                    binding.buttonControl.setText(R.string.local_video_button_pause);
                    player.start();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        player.stop();
        player.release();
        player = null;
    }
}