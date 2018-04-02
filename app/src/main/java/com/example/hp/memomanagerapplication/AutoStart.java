package com.example.hp.memomanagerapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hp on 02/04/2018.
 */

public class AutoStart extends BroadcastReceiver
{
    //AlarmReciever alarm = new AlarmReciever();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            //alarm.setAlarm(context);
        }
    }
}