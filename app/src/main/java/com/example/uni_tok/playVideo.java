//-----------------Kostas Start-----------------//
package com.example.uni_tok;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class playVideo extends AppCompatActivity {

    private EditText search_bar;
    private VideoView myVideo;
    private String filepath;
    private MediaController media;
    private String parent;
    private static int failed_attempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_data);

        failed_attempts = 0;

        search_bar = (EditText) findViewById(R.id.search_bar);

        Bundle getUri = this.getIntent().getExtras();
        filepath = getUri.getString("filepath");
        parent = getUri.getString("context");
        Log.d("CONTEXT", parent);

        myVideo = (VideoView) findViewById(R.id.videoViewForPlayData);
        myVideo.setVideoPath(filepath);

        //controller(pause, navigate forward, navigate backward)
        media = new MediaController(this);
        myVideo.setMediaController(media);
        media.setAnchorView(myVideo);

        //start the video
        myVideo.start();
    }

    public void Back(View v){
        //Not right, I don't know already what parent contains.
        if (parent.equals("SearchActivity")){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else if (parent.equals("runUser")){
            Intent intent = new Intent(this, runUser.class);
            startActivity(intent);
        }
    }
}
//-----------------Kostas End-----------------//