package com.example.uni_tok;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class SetChannelBrokerWorker extends Worker {

    public SetChannelBrokerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String channelName;
        boolean unique;
        Data outputData;

        try {
            TimeUnit.SECONDS.sleep(3);
            channelName = getInputData().getString("ChannelName");
            unique = AppNodeImpl.setChannelBroker(channelName);

        } catch (InterruptedException ie) {
            Log.d("IE", ie.getMessage());
            return Result.failure();
        } catch (IOException | ClassNotFoundException io) {
            Log.d("IO", io.getMessage());
            return Result.failure();
        }
        outputData = new Data.Builder().putBoolean("UNIQUE", unique).build();
        return Result.success(outputData);
    }
}