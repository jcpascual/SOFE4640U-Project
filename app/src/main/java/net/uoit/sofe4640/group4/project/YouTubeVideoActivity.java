package net.uoit.sofe4640.group4.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.databinding.ActivityYoutubeVideoBinding;

public class YouTubeVideoActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityYoutubeVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityYoutubeVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarYoutubeVideo);

        // Get the video ID from the Intent.
        Intent intent = getIntent();
        String videoId = intent.getStringExtra("videoId");

        // JavaScript is disabled by default. We need this to load a YouTube video.
        binding.webView.getSettings().setJavaScriptEnabled(true);

        // Load the embedded video page.
        binding.webView.loadUrl("https://www.youtube.com/embed/" + videoId + "?autoplay=1");
    }
}