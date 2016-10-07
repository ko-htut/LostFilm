package com.alexandr.lostfilm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alexandr.lostfilm.task.AllSerialUpdateTask;

public class UpdateAllReceiver extends BroadcastReceiver {
    public static String ACTION_ALL_UPDATE="com.alexandr.lostfilm.ALL_UPDATE";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_ALL_UPDATE))
        {
            Log.i("debugReceiver", "UpdateAllReceiver onReceive");
            AllSerialUpdateTask update = new AllSerialUpdateTask();
            update.execute(context);
        }
    }
}
