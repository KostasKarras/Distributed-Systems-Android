package com.example.uni_tok;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ServerWorker extends Worker{

    public ServerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        /*
        try{
            int x;
            for (x = 1; x < 11; x++) {
                final int y = x;
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "MESSAGE PRINT",
                                Toast.LENGTH_SHORT).show();
                    }
@@ -44,6 +51,7 @@
        }
        */

        AppNodeImpl.init(4960);

        AppNodeImpl.handleRequest();

        return Result.success();



    }
}
