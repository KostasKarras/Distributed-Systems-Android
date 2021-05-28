package com.example.uni_tok;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SearchActivity extends AppCompatActivity {

    EditText search_bar;
    Button subscribeButton;
    SharedPreferences sharedPreferences;
    TextView relatedTopic;

    private static final int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CREATION", "I am in search activity!\n");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        sharedPreferences = getApplicationContext()
                           .getSharedPreferences("appdata", MODE_PRIVATE);
        search_bar = (EditText)findViewById(R.id.search_bar);
        subscribeButton = (Button)findViewById(R.id.subscribeButton);
        relatedTopic = (TextView)findViewById(R.id.topicTextview);

        String topic = sharedPreferences.getString("searchKey", "None");
        relatedTopic.setText(topic);

    }

    public void subscribeAction(View v) {
        Button button = (Button) v;
        if (button.getText().equals("Subscribe")) {
            button.setBackgroundColor(getResources().getColor(R.color.gray));
            button.setText(R.string.subscribedText);
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.app_color));
            button.setText(R.string.subscribeText);
        }
    }

    public void channelActivity(View v) {
        Intent intent = new Intent(this, ChannelActivity.class);

        //To check the activity in the channel
        intent.putExtra("upload", false);

        startActivity(intent);
    }

    public void searchActivity(View v) {

        //STORE SEARCH TOPIC
        String topic = search_bar.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("searchKey", topic );
        editor.apply();

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void uploadVideoActivity(View v) {
        if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
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
