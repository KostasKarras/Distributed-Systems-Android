package com.example.uni_tok;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class UploadVideoActivity extends Activity {

    private static final int GALLERY_REQUEST_CODE = 2000;
    private VideoView videoView;
    private Button playButton;
    private ArrayList<Uri> videos = new ArrayList<Uri>();;
    private ArrayAdapter<String> arrayAdapter;
    private EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        search_bar = (EditText) findViewById(R.id.search_bar);

        Intent videoPicker = new Intent();
        videoPicker.setAction(Intent.ACTION_GET_CONTENT);
        videoPicker.setType("video/*");
        startActivityForResult(Intent.createChooser(videoPicker, "Pick a video"), GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            MediaController media = new MediaController(this);

            Uri videoData = data.getData();
            VideoView myVideo = (VideoView) findViewById(R.id.videoView);
            myVideo.setVideoURI(videoData);

            //controller(pause, navigate forward, navigate backward)
            myVideo.setMediaController(media);
            media.setAnchorView(myVideo);

            //add video in arraylist
            videos.add(videoData);

            //start the video
            myVideo.start();
        }
    }

    public void channelActivity(View v) {
        Intent intent = new Intent(this, ChannelActivity.class);
        startActivity(intent);
    }

    public void searchActivity(View v) {
        String topic = search_bar.getText().toString();
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void uploadVideoActivity(View v) {
        Intent intent = new Intent(this, UploadVideoActivity.class);
        startActivity(intent);
    }

    public void homeActivity(View v) {
        Intent intent = new Intent(this, runUser.class);
        startActivity(intent);
    }

    public void exit(View v) {}

    public void uploadActivity(View view) {
        Intent intent = new Intent(this, UploadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("videos",(ArrayList<Uri>) videos);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
