package com.alexandr.lostfilm.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.alexandr.lostfilm.task.AllSerialUpdateTask;

import java.util.Calendar;

public class OnBootCompleteReceiver extends BroadcastReceiver {
    public OnBootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i("debugOnboot","onBoot");

            Intent actionIntentAll = new Intent(context.getApplicationContext(), UpdateAllReceiver.class);
            actionIntentAll.setAction(UpdateAllReceiver.ACTION_ALL_UPDATE);
            PendingIntent pendingIntentAll = PendingIntent.getBroadcast(context.getApplicationContext(),0,actionIntentAll,PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmMgr = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_DAY, pendingIntentAll);


            Intent actionIntentFav = new Intent(context.getApplicationContext(), UpdateFavReceiver.class);
            actionIntentFav.setAction(UpdateFavReceiver.ACTION_FAV_UPDATE);
            PendingIntent pendingIntentFav = PendingIntent.getBroadcast(context.getApplicationContext(),0,actionIntentFav,PendingIntent.FLAG_CANCEL_CURRENT);
            alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntentFav);

            Log.i("debugAlarm","Setted");
        }
    }
}
