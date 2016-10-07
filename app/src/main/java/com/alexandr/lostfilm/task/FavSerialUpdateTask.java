package com.alexandr.lostfilm.task;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.alexandr.lostfilm.MainActivity;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.fragment.FragmentFavorite;
import com.alexandr.lostfilm.inet.WebParser;
import com.alexandr.lostfilm.notification.NotificationDisplay;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by alexandr on 01/09/16.
 */
public class FavSerialUpdateTask extends AsyncTask<Context,Void,Void> {
    MainActivity mActivity=null;
    Context mCtx;
    ArrayList<NotificationDisplay> notifList = new ArrayList<>();
    NotificationDisplay toAdd;
    @Override
    protected void onPreExecute() {
        Log.i("debugAsynkUpdateFav","start");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("debugAsynkUpdateFav","stop");
        if (mActivity!=null)
        {
            FragmentFavorite fragmentFav = (FragmentFavorite)
                    mActivity.getSupportFragmentManager().findFragmentByTag(FragmentFavorite.FRAGMENT_TAG);
            if (fragmentFav != null) {
                fragmentFav.swipeRefreshLayout.setRefreshing(false);
                fragmentFav.refreshRecyclerView();
            }
        }

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
        Log.i("debugAsynkUpdateFav","doInBackground");
        mCtx = params[0];
        if (mCtx instanceof MainActivity)
        {
            mActivity = (MainActivity) mCtx;
        }
        DB db = new DB(mCtx);
        WebParser webParser = new WebParser(mCtx);
        db.openWritable();
        Cursor c = db.getFavSerials();

        int ruName = c.getColumnIndex(DB.ALL_COLUMN_RU_NAME);
        int date = c.getColumnIndex(DB.ALL_COLUMN_DATE);
        int link = c.getColumnIndex(DB.ALL_COLUMN_LINK);
        notifList.clear();
        if (c.moveToFirst()) {
            do
            {
                toAdd=webParser.parseFavSerial(c.getString(link),c.getString(date),c.getString(ruName));
                if(toAdd!=null)
                {
                    notifList.add(toAdd);
                }
            } while (c.moveToNext());
            webParser.close();
        }
        return null;
    }

}

