package com.alexandr.lostfilm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.alexandr.lostfilm.receivers.CheckNewSerialsReceiver;
import com.alexandr.lostfilm.receivers.UpdateAllReceiver;
import com.alexandr.lostfilm.receivers.UpdateFavReceiver;

class AlarmManagerScheduler {
    public AlarmManagerScheduler(Context mCtx) {
        this.mCtx = mCtx;
    }

    private Context mCtx;
    private Intent actionIntentAll;
    private Intent actionIntentFav;
    private Intent actionIntentCheckNew;

    public void setAlarms() {
        actionIntentAll = new Intent(mCtx.getApplicationContext(), UpdateAllReceiver.class);
        actionIntentFav = new Intent(mCtx.getApplicationContext(), UpdateFavReceiver.class);
        actionIntentCheckNew = new Intent(mCtx.getApplicationContext(), CheckNewSerialsReceiver.class);
        if (checkAlarm(actionIntentAll, PendingIntent.FLAG_UPDATE_CURRENT))
        {
            Log.i("DEBUG_ALARM","All setted");
            setAlarm(actionIntentAll,PendingIntent.FLAG_UPDATE_CURRENT,
                    UpdateAllReceiver.ACTION_ALL_UPDATE,AlarmManager.INTERVAL_DAY);
        }
        if (checkAlarm(actionIntentFav, PendingIntent.FLAG_UPDATE_CURRENT))
        {
            Log.i("DEBUG_ALARM","Fav setted");
            setAlarm(actionIntentFav,PendingIntent.FLAG_UPDATE_CURRENT,
                    UpdateFavReceiver.ACTION_FAV_UPDATE,AlarmManager.INTERVAL_FIFTEEN_MINUTES);
        }
        if (checkAlarm(actionIntentCheckNew, PendingIntent.FLAG_UPDATE_CURRENT))
        {
            Log.i("DEBUG_ALARM","Check setted");
            setAlarm(actionIntentCheckNew,PendingIntent.FLAG_UPDATE_CURRENT,
                    CheckNewSerialsReceiver.ACTION_CHECK_NEW,AlarmManager.INTERVAL_DAY);
        }

    }

   public void setAlarm (Intent intent, int flag, String action,long interval)
    {
        AlarmManager alarmMgr = (AlarmManager)mCtx.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mCtx.getApplicationContext(),0,intent,flag);
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),interval, pendingIntent);
    }
    public boolean checkAlarm(Intent intent, int flag)
    {
        return PendingIntent.getBroadcast(mCtx.getApplicationContext(), 0, intent,flag) != null;
    }
}