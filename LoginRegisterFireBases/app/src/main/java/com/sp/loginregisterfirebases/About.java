package com.sp.loginregisterfirebases;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //Don't show the title

        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.about; // Replace "your_video_file_name" with the actual name of your video file
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();
    }
}