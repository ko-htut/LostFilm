package com.alexandr.lostfilm.task;

import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.inet.WebParser;
import com.alexandr.lostfilm.notification.NotificationDisplay;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by alexandr on 05/09/16.
 */
public class CheckNewSerialsTask extends AsyncTask<Context,Void,Void> {
    Context mCtx;
    ArrayList<NotificationDisplay> notifList;

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("debugAsynk","check stop");
        NotificationManager mNotificationManager =
                (NotificationManager) mCtx.getSystemService(mCtx.NOTIFICATION_SERVICE);
        Log.i("debugSize",String.valueOf(notifList.size()));
        for (NotificationDisplay nd:notifList)
        {
            Glide.with( mCtx.getApplicationContext() ) // safer!
                    .load( nd.getImg() )
                    .asBitmap()
                    .into( nd.getTarget() );

            mNotificationManager.notify(nd.getId(),nd.getNotif());
        }
    }

    @Override
    protected Void doInBackground(Context... params) {
        mCtx = params[0];
        Log.i("debugAsynk","doInBackgroundCheck");
        WebParser webParser = new WebParser(mCtx);
        notifList=webParser.parseAllSerials();
        webParser.close();
        return null;
    }
}
