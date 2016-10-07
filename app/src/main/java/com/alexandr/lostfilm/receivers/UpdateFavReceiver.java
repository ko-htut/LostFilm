package com.alexandr.lostfilm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alexandr.lostfilm.task.AllSerialUpdateTask;
import com.alexandr.lostfilm.task.FavSerialUpdateTask;

public class UpdateFavReceiver extends BroadcastReceiver {
    public static String ACTION_FAV_UPDATE="com.alexandr.lostfilm.FAV_UPDATE";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_FAV_UPDATE))
        {
            Log.i("debugReceiver", "UpdateFavReceiver onReceive");
            FavSerialUpdateTask update = new FavSerialUpdateTask();
            update.execute(context);
        }

    }
}
