package com.example.hp.memomanagerapplication;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by hp on 02/04/2018.
 */

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReciever:", "onRecieve");
        Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
        /*
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Project Manager")
                .setContentText("WAZZUP")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(0, mBuilder.build());
        */
        //TODO: SEND NOTIFICATION
    }

    private void sendNotification() {


    }

}
