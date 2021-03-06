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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
        String videoName;
        String path;
        String [] hashtags;
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
                    boolean fetched_successfully;
                    topic = getInputData().getString("TOPIC");
                    if (topic != null) {
                        fetched_successfully = AppNodeImpl.setSearchTopicVideoList(topic);
                        if (fetched_successfully) return Result.success();
                    }
                    break;

                case "Upload":
                    path = getInputData().getString("path");
                    videoName = getInputData().getString("videoName");
                    hashtags = getInputData().getStringArray("associatedHashtags");
                    ArrayList<String> associatedHashtags = new ArrayList<>();
                    if (hashtags != null) {
                        Collections.addAll(associatedHashtags, hashtags);
                        boolean successful_upload = AppNodeImpl.Upload(path, associatedHashtags, videoName);
                        if (successful_upload) return Result.success();
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
