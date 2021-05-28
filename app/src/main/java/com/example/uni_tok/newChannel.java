package com.example.uni_tok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class newChannel extends AppCompatActivity {

    EditText channelName;
    Button submitButton;
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);

        sharedPreferences = getApplicationContext()
                           .getSharedPreferences("appdata", MODE_PRIVATE);

        channelName = (EditText) findViewById(R.id.ChannelName);

        submitButton = (Button) findViewById(R.id.channelButton);
        submitButton.setOnClickListener(v -> runUser());
    }
/*
    public void runUser(){

        //STORE CHANNEL NAME
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("channelKey", channelName.getText().toString() );
        editor.apply();

        Intent intent = new Intent(this, runUser.class);
        startActivity(intent);
        finish();
    }

 */

    public void runUser() {

        Data data = new Data.Builder()
                            .putString("ChannelName", channelName.getText().toString())
                            .build();

        OneTimeWorkRequest oneTimeRequest = new OneTimeWorkRequest.Builder(SetChannelBrokerWorker.class)
                .setInputData(data)
                .build();

        Toast.makeText(getApplicationContext(), "Starting worker...", Toast.LENGTH_SHORT)
                .show();
        WorkManager.getInstance(this)
                .enqueueUniqueWork("Connect to Broker",
                        ExistingWorkPolicy.KEEP, oneTimeRequest);
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeRequest.getId())
                .observe(this, workInfo -> {
                    Log.d("State", workInfo.getState().name());
                    if ( workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        boolean unique = workInfo.getOutputData()
                                                 .getBoolean("UNIQUE", false);
                        if (!unique) {
                            Toast.makeText(getApplicationContext(), "Channel Name exists. Give another!",
                                           Toast.LENGTH_SHORT)
                                 .show();
                        } else {
                            Intent intent = new Intent(this, runUser.class);
                            startActivity(intent);
                            finish();
                        }

                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                        Toast.makeText(getApplicationContext(),
                                "Something wrong happened" +
                                        "Try again.", Toast.LENGTH_SHORT).show();
                        Log.d("Status", "Status failed");
                    }

                });




    }
}