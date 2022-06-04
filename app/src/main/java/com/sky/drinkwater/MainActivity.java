package com.sky.drinkwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sky.drinkwater.services.PeriodicWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        setupButton();

    }

    private void setupButton(){
        Button hydrateButton = findViewById(R.id.switchHydration);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        if(sharedPref.getBoolean(getString(R.string.preferenceWorking), false)){
            hydrateButton.setText("Turn reminder off\nStill, remember to hydrate!");

            hydrateButton.setOnClickListener(view -> {
                switchWorker(false);
                sharedPref.edit().putBoolean(getString(R.string.preferenceWorking), false).apply();
                setupButton();
            });
        } else {
            hydrateButton.setText("Turn on reminder!");

            hydrateButton.setOnClickListener(view -> {
                switchWorker(true);
                sharedPref.edit().putBoolean(getString(R.string.preferenceWorking), true).apply();
                setupButton();
            });
        }
    }

    private void switchWorker(boolean turnOn){
        if(turnOn) {
            PeriodicWorkRequest.Builder myWorkBuilder =
                    new PeriodicWorkRequest.Builder(PeriodicWorker.class, 15, TimeUnit.MINUTES);
            PeriodicWorkRequest myWork = myWorkBuilder.build();
            WorkManager.getInstance()
                    .enqueueUniquePeriodicWork(getString(R.string.WorkerTag),
                            ExistingPeriodicWorkPolicy.KEEP, myWork);
        } else {
            WorkManager.getInstance().cancelUniqueWork(getString(R.string.WorkerTag));
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = "ASDasdasdasdas";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_name), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}