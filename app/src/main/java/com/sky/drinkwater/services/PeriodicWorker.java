package com.sky.drinkwater.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.sky.drinkwater.MainActivity;
import com.sky.drinkwater.R;

public class PeriodicWorker extends Worker {

    private static final String TAG = PeriodicWorker.class.getSimpleName();
    Context context;

    public PeriodicWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        context = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {

        //simulate slow work
        // WorkerUtils.makeStatusNotification("Fetching Data", applicationContext);
        Log.i(TAG, "Fetching Data from Remote host");

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context, context.getString(R.string.channel_name))
                .setSmallIcon(R.drawable.hydration)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
        return Result.success();
    }


    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this worker");
    }
}