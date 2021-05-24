package com.example.uni_tok;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity {

    EditText search_bar;
    ArrayAdapter<Uri> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_channel);

        search_bar = (EditText)findViewById(R.id.search_bar);

        Bundle getUri = this.getIntent().getExtras();
        ArrayList<Uri> videoList = getUri.getParcelableArrayList("videosToChannel");

//        Bundle getVideoName = this.getIntent().getExtras();
//        ArrayList<String> videoNames = (ArrayList<String>) getVideoName.getSerializable("videoName");
//
//        Bundle getHashtags = this.getIntent().getExtras();
//        ArrayList<String []> associatedHashtags = (ArrayList<String[]>) getHashtags.getSerializable("hashtags");

        ListView lv = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoList);
        lv.setAdapter(arrayAdapter);
    }

    public void channelActivity(View v) {}

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

}
