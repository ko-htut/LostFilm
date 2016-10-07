package com.alexandr.lostfilm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alexandr.lostfilm.task.CheckNewSerialsTask;

public class CheckNewSerialsReceiver extends BroadcastReceiver {
    public static String ACTION_CHECK_NEW="com.alexandr.lostfilm.CHECK_NEW";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_CHECK_NEW))
        {
            Log.i("debugReceiver", "CheckNewReceiver onReceive");
            CheckNewSerialsTask check = new CheckNewSerialsTask();
            check.execute(context);
        }
    }
}
