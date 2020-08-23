package com.example.todoapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        sendNotification();
    }

    private void sendNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("channel1","remainder", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is to notify the remainder");

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
