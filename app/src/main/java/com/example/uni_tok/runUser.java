package com.example.uni_tok;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import org.w3c.dom.Text;

public class runUser extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 1;

    TabHost tab;
    ViewPager viewPager;

    ImageButton profile_button;
    ImageButton exit_button;
    ImageButton home_button;
    ImageButton search_button;
    ImageButton upload_button;
    EditText search_bar;

    SharedPreferences sharedPreferences;

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.addTab(tabLayout.newTab().setText("Upload"));
        tabLayout.addTab(tabLayout.newTab().setText("Channel"));

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
   */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CREATION", "I am in onCreate of runUser!\n");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        sharedPreferences = getApplicationContext()
                .getSharedPreferences("appdata", MODE_PRIVATE);

        search_bar = (EditText)findViewById(R.id.search_bar);

        /*

        profile_button = (ImageButton)findViewById(R.id.profileButton);
        exit_button = (ImageButton)findViewById(R.id.exitButton);
        home_button = (ImageButton)findViewById(R.id.homeButton);
        search_button = (ImageButton)findViewById(R.id.search_button);
        upload_button = (ImageButton)findViewById(R.id.uploadButton);

        /*
        profile_button.setOnClickListener(this);
        exit_button.setOnClickListener(this);
        home_button.setOnClickListener(this);
        search_button.setOnClickListener(this);
        upload_button.setOnClickListener(this);

         */

        // ---------------- MICHALIS TEST CODE ------------------- //
        OneTimeWorkRequest oneTimeRequest = new OneTimeWorkRequest.Builder(ServerWorker.class)
                .build();

        WorkManager.getInstance(this)
                .enqueueUniqueWork("Test server thread",
                        ExistingWorkPolicy.KEEP, oneTimeRequest);


        // ---------------- END OF MICHALIS TEST CODE ------------------- //

    }

    /*
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.profileButton:
                intent = new Intent(this, ChannelActivity.class);
                startActivity(intent);
                break;
            case R.id.exitButton:
                break;
            case R.id.homeButton:
                break;
            case R.id.search_button:
                String topic = search_bar.getText().toString();
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.uploadButton:
                break;
        }

    }

     */

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
        if (ActivityCompat.checkSelfPermission(runUser.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(runUser.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
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

    public void homeActivity(View v) {}

    public void exit(View v) {}
}