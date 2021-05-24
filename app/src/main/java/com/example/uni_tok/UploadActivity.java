package com.example.uni_tok;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class UploadActivity extends AppCompatActivity {

    EditText search_bar;
    EditText videoNameEditText;
    EditText hashtagsEditText;
    ArrayList<Uri> videos;
    ArrayList<String> videoNames = new ArrayList<>();
    ArrayList<String[]> associatedHashtags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Bundle getBundle = this.getIntent().getExtras();
        videos = getBundle.getParcelableArrayList("videos");

        search_bar = (EditText) findViewById(R.id.search_bar);

        //Get video name
        videoNameEditText = (EditText) findViewById(R.id.videoNameEditText);
        String videoName = videoNameEditText.getText().toString();
        videoNames.add(videoName);

        //Get hashtags
        hashtagsEditText = (EditText) findViewById(R.id.hashtagsEditText);
        String hashtags = hashtagsEditText.getText().toString();
        String [] tempAssociatedHashtags = hashtags.split(" ");
        associatedHashtags.add(tempAssociatedHashtags);
    }

    public void channelActivity(View v) {
        Intent intent = new Intent(this, ChannelActivity.class);

        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("videosToChannel",(ArrayList<Uri>) videos);
        intent.putExtras(bundle);

//        bundle.putSerializable("videoName", videoNames);
//        intent.putExtras(bundle);
//
//        bundle.putSerializable("hashtags", associatedHashtags);
//        intent.putExtras(bundle);

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
        startActivity(intent);
    }

}