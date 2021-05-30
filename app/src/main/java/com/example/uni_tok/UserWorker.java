package com.example.uni_tok;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class UserWorker extends Worker {

    public UserWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String topic;
        String action;
        SocketAddress socketAddress;

        try {
            TimeUnit.SECONDS.sleep(3);
            action = getInputData().getString("ACTION");
            if (action == null) return Result.failure();

            switch (action) {

                case "SUBSCRIBE":
                    boolean successful_subscription;
                    topic = getInputData().getString("TOPIC");
                    if (topic != null) {
                        socketAddress = AppNodeImpl.hashTopic(topic);
                        successful_subscription = AppNodeImpl.register(socketAddress, topic);
                        if (successful_subscription) return Result.success();
                        break;
                    }

                case "SUBSCRIBED":
                    boolean successful_unsubscription;
                    topic = getInputData().getString("TOPIC");
                    if (topic!=null) {
                        socketAddress = AppNodeImpl.hashTopic(topic);
                        successful_unsubscription = AppNodeImpl.unregister(socketAddress, topic);
                        if (successful_unsubscription) return Result.success();
                        break;
                    }
            }

        }
        catch (InterruptedException ie) {
                Log.d("IE", ie.getMessage());
        }

        return Result.failure();

    }


    @Override
    public void onStopped() {
        Log.d("STATE", "STOPPED");
        super.onStopped();
    }
}
