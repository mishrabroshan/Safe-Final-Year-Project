package com.rsm.safe;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.rsm.safe.Constants.ConstantsVariables;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    ConstantsVariables.CHANNELID,
                    "Safe Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This is Notification Channel For Safe App");
            NotificationManager notificationManager =  getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
