package com.example.uni_tok;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.Set;

public class ChannelActivity extends AppCompatActivity {

    EditText search_bar;
    ArrayAdapter<Uri> arrayAdapter;
    TextView channelName;
    TextView videoTitle;
    TextView videoHashtags;
    SharedPreferences sharedPreferences;

    int failed_attempts = 0;

    private static final int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CREATION", "I am in channel activity!\n");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_channel);

        search_bar = (EditText)findViewById(R.id.search_bar);

        //LOAD CHANNEL NAME
        channelName = (TextView)findViewById(R.id.channelNameTextview);
        String name = AppNodeImpl.getChannel().getChannelName();
        channelName.setText(name);

        if (this.getIntent().getBooleanExtra("upload", true)) {
            //LOAD VIDEO NAME
            videoTitle = (TextView) findViewById(R.id.videoTitle);
            sharedPreferences = getApplicationContext().
                    getSharedPreferences("appdata", MODE_PRIVATE);
            name = sharedPreferences.getString("videoTitle", "None");
            videoTitle.setText(name);

            //LOAD HASHTAGS
            videoHashtags = (TextView) findViewById(R.id.hashtags);
            sharedPreferences = getApplicationContext().
                    getSharedPreferences("appdata", MODE_PRIVATE);
            name = sharedPreferences.getString("hashtags", "None");
            videoHashtags.setText(name);

            Bundle getUri = this.getIntent().getExtras();
            ArrayList<Uri> videoList = getUri.getParcelableArrayList("videosToChannel");

//            ListView lv = (ListView) findViewById(R.id.listView);
//            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoList);
//            lv.setAdapter(arrayAdapter);
        }
    }

    public void channelActivity(View v) {}

    public void searchActivity(View v) {

        String topic = search_bar.getText().toString();
        String action = "TOPIC VIDEO LIST";
        Intent intent = new Intent(this, SearchActivity.class);

        Data data = new Data.Builder()
                .putString("TOPIC", search_bar.getText().toString())
                .putString("ACTION", action)
                .build();

        OneTimeWorkRequest topicRequest = new OneTimeWorkRequest.Builder(UserWorker.class)
                .setInputData(data)
                .build();

        String uniqueWorkName = "Topic_from_Channel"+ Integer.toString(failed_attempts);
        failed_attempts += 1;

        WorkManager.getInstance(this)
                .enqueueUniqueWork(uniqueWorkName, ExistingWorkPolicy.REPLACE, topicRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(topicRequest.getId())
                .observe(this, workInfo -> {

                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        Log.d("STATE", "SUCCEEDED");
                        startActivity(intent);
                    } else if(workInfo.getState() == WorkInfo.State.FAILED) {
                        Log.d("STATE", "FAILED");
                        Toast.makeText(getApplicationContext(), "Error in fetching results..",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        //STORE SEARCH TOPIC
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("searchKey", topic );
        editor.apply();

    }

    public void uploadVideoActivity(View v) {
        if (ActivityCompat.checkSelfPermission(ChannelActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChannelActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            Intent intent = new Intent(this, UploadVideoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, UploadVideoActivity.class);
            startActivity(intent);
        }
    }

    public void homeActivity(View v) {

        Intent intent = new Intent(this, runUser.class);
        startActivity(intent);

    }

    public void exit(View v) {}

}
