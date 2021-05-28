package com.example.uni_tok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText IP;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IP = (EditText) findViewById(R.id.AddressKeeperIP);

        submitButton = (Button) findViewById(R.id.AddressKeeperSubmit);
        submitButton.setOnClickListener((View v) -> newChannel());

    }
/*
    public void newChannel(){
        Intent intent = new Intent(this, newChannel.class);
        startActivity(intent);
        finish();
    }

 */
    public void newChannel() {

        Data data = new Data.Builder().putString("AddressKeeperIP", IP.getText().toString())
                            .build();
        Log.d("M1", IP.getText().toString());

        OneTimeWorkRequest oneTimeRequest = new OneTimeWorkRequest.Builder(FirstConnectionWorker.class)
                                                                  .setInputData(data)
                                                                  .build();

        Toast.makeText(getApplicationContext(), "Starting worker...", Toast.LENGTH_SHORT)
             .show();

        WorkManager.getInstance(this)
                   .enqueueUniqueWork("Connect to address Keeper",
                           ExistingWorkPolicy.REPLACE, oneTimeRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeRequest.getId())
                   .observe(this, workInfo -> {
                       Log.d("State", workInfo.getState().name());
                       if ( workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                           Intent intent = new Intent(getApplicationContext(), newChannel.class);
                           startActivity(intent);
                           finish();
                       } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                           Toast.makeText(getApplicationContext(),
                                   "Couldn't connect to Address Keeper. " +
                                   "Try again.", Toast.LENGTH_SHORT).show();
                           Log.d("Status", "Status failed");

                       }

                   });


    }


}