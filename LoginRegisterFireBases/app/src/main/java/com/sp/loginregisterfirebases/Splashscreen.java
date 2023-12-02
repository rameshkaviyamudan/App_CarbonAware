package com.sp.loginregisterfirebases;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


public class Splashscreen extends AppCompatActivity {

    private static final long SPLASH_DELAY = 4000;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Remove title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

// Remove title from window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        setContentView(R.layout.activity_splashscreen);


        // Initialize the MediaPlayer with the music file from the raw folder
        mediaPlayer = MediaPlayer.create(this, R.raw.intro); // Replace "your_music_file" with the actual file name

        // Start playing the music
        mediaPlayer.start();

        // Use a handler to post a runnable after the specified delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the delay
                Intent intent = new Intent(Splashscreen.this, Login.class);
                startActivity(intent);

                // Finish the splash activity so the user won't go back to it
                finish();
            }
        }, SPLASH_DELAY);
    }

    // Method to skip the splash screen when the user clicks on the clickable element
    public void skipSplashScreen(View view) {
        // Start the main activity immediately
        Intent intent = new Intent(Splashscreen.this, Login.class);
        startActivity(intent);

        // Finish the splash activity so the user won't go back to it
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}