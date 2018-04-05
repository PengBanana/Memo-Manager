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
        Log.d("AlarmReciever:","onReceive-START");
        MySQLiteHelper db = new MySQLiteHelper(context);
        //int ongoingCount=db.getOngoingCount();
        int overdueCount=db.getOverdueCount();
        int activeCount=db.getActiveCount();
        String text ="You have "+overdueCount+" overdue and "+activeCount+" active projects";
        //int ongoingCount=0;
        //Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
        if(overdueCount>0&&activeCount>0){
            text="You have "+overdueCount+" overdue and "+activeCount+" active projects";
        }
        else if(overdueCount>0&&activeCount<1){
            text="You have "+overdueCount+" overdue projects";
        }
        else if(activeCount>0&&overdueCount<1){
            text="You have "+activeCount+" active projects";
        }
        if(overdueCount>0||activeCount>0){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context
            )
                    .setSmallIcon(R.mipmap.memo_manager_icon)
                    .setContentTitle("Project Manager")
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            notificationManager.notify(0, mBuilder.build());
            Log.d("AlarmReciever:","onReceive-END");
        }
    }

}
