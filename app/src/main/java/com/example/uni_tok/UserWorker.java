package com.example.uni_tok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.shapes.PathShape;
import android.util.Log;
import android.webkit.HttpAuthHandler;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.lang.reflect.Type;
import java.net.SocketAddress;
import java.util.HashMap;
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
            TimeUnit.SECONDS.sleep(1);
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
                    }
                    break;


                case "SUBSCRIBED":
                    boolean successful_unsubscription;
                    topic = getInputData().getString("TOPIC");
                    if (topic!=null) {
                        socketAddress = AppNodeImpl.hashTopic(topic);
                        successful_unsubscription = AppNodeImpl.unregister(socketAddress, topic);
                        if (successful_unsubscription) return Result.success();
                    }
                    break;

                case "TOPIC VIDEO LIST" :
                    HashMap<ChannelKey, String> videoList;
                    topic = getInputData().getString("TOPIC");
                    if (topic != null) {
                        //videoList = AppNodeImpl.getTopicVideoList(topic);
                        //TEST CODE
                        HashMap<Integer, String> testMap = new HashMap<>();
                        testMap.put(10, "John");
                        testMap.put(20, "Michael");
                        testMap.put(30, "George");
                        //
                    }
                    break;
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
