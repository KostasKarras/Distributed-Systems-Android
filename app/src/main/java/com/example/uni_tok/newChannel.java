package com.example.uni_tok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
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

import java.util.concurrent.TimeUnit;

import kotlin.coroutines.Continuation;

public class newChannel extends AppCompatActivity {

    EditText channelName;
    Button submitButton;
    SharedPreferences sharedPreferences;
    OneTimeWorkRequest oneTimeRequest;
    WorkManager workManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);

        sharedPreferences = getApplicationContext()
                           .getSharedPreferences("appdata", MODE_PRIVATE);

        channelName = (EditText) findViewById(R.id.ChannelName);

        submitButton = (Button) findViewById(R.id.channelButton);
        submitButton.setOnClickListener(v -> runUser());

        Data data = new Data.Builder()
                .putString("ChannelName", channelName.getText().toString())
                .build();

        oneTimeRequest = new OneTimeWorkRequest.Builder(SetChannelBrokerWorker.class)
                .keepResultsForAtLeast(1, TimeUnit.SECONDS)
                .addTag("REQUEST")
                .setInputData(data)
                .build();

        workManager = WorkManager.getInstance(this);
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

        Log.d("RUN USER", "CALLED");
        /*
        Data data = new Data.Builder()
                            .putString("ChannelName", channelName.getText().toString())
                            .build();

        oneTimeRequest = new OneTimeWorkRequest.Builder(SetChannelBrokerWorker.class)
                .keepResultsForAtLeast(1, TimeUnit.SECONDS)
                .setInputData(data)
                .build();

*/

        Toast.makeText(getApplicationContext(), "Starting worker...", Toast.LENGTH_SHORT)
                .show();

        workManager.enqueueUniqueWork("Connect to Broker",
                        ExistingWorkPolicy.REPLACE, oneTimeRequest);

        workManager.getWorkInfoByIdLiveData(oneTimeRequest.getId())
                .observe(this, workInfo -> {
                    Log.d("State", workInfo.getState().name());
                    if ( workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                        Intent intent = new Intent(this, runUser.class);
                        startActivity(intent);
                        finish();

                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                        String error = workInfo.getOutputData().getString("ERROR");
                        if (error.equals("EXCEPTION")) {
                            Toast.makeText(getApplicationContext(),
                                    "Something wrong happened" +
                                            "Try again.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "Channel name already exists." +
                                            "Try again.", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Status", "Status failed");

                    }

                });

        Log.d("RUN USER", "STOPPED");


    }
}