package com.example.uni_tok;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UploadActivity extends AppCompatActivity {

    EditText search_bar;
    EditText videoNameEditText;
    EditText hashtagsEditText;
    ArrayList<Uri> videos = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        search_bar = (EditText) findViewById(R.id.search_bar);

        sharedPreferences = getApplicationContext()
                .getSharedPreferences("appdata", MODE_PRIVATE);

        Bundle getUri = this.getIntent().getExtras();
        videos = getUri.getParcelableArrayList("videos");

        //Get video name
        videoNameEditText = (EditText) findViewById(R.id.videoNameEditText);

        //Get hashtags (need split!!!)
        hashtagsEditText = (EditText) findViewById(R.id.hashtagsEditText);
    }

    public void channelActivity(View v) {
        Intent intent = new Intent(this, ChannelActivity.class);

        //To check the activity in the channel
        intent.putExtra("upload", true);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("videosToChannel",(ArrayList<Uri>) videos);
        intent.putExtras(bundle);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("videoTitle", videoNameEditText.getText().toString());
        editor.apply();

        editor.putString("hashtags", hashtagsEditText.getText().toString());
        editor.apply();

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